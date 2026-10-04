const state = {
  token: sessionStorage.getItem("oficina_token") || "",
  email: sessionStorage.getItem("oficina_email") || "",
  view: "overview",
  orders: [],
  peopleTab: "clients",
  stockTab: "parts",
  orderFilter: "all",
  clients: [],
  vehicles: [],
  parts: [],
  services: [],
  employees: []
};

const $ = (selector, root = document) => root.querySelector(selector);
const $$ = (selector, root = document) => [...root.querySelectorAll(selector)];
const esc = (value) => String(value ?? "").replace(/[&<>"']/g, (char) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#039;" }[char]));
const money = (value) => Number(value || 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
const date = (value) => value ? new Date(value).toLocaleDateString("pt-BR") : "—";
const toast = (message, error = false) => {
  const el = $("#toast"); el.textContent = message; el.className = `toast show${error ? " error" : ""}`;
  window.clearTimeout(toast.timer); toast.timer = window.setTimeout(() => el.className = "toast", 3500);
};

async function request(path, options = {}) {
  const headers = { ...(options.body ? { "Content-Type": "application/json" } : {}), ...(state.token ? { Authorization: `Bearer ${state.token}` } : {}), ...(options.headers || {}) };
  const response = await fetch(path, { ...options, headers });
  const text = await response.text();
  let data = null;
  try { data = text ? JSON.parse(text) : null; } catch { data = text; }
  if (!response.ok) {
    if (response.status === 401) { logout(); }
    const message = typeof data === "object" && data ? (data.message || data.error || JSON.stringify(data)) : data;
    throw new Error(message || `Erro ${response.status}`);
  }
  return data;
}

function showApp() {
  $("#login-screen").classList.add("hidden"); $("#app-shell").classList.remove("hidden");
  $("#user-email").textContent = state.email || "Sessão ativa"; navigate(state.view);
}
function logout() {
  state.token = ""; sessionStorage.removeItem("oficina_token"); sessionStorage.removeItem("oficina_email");
  $("#app-shell").classList.add("hidden"); $("#login-screen").classList.remove("hidden");
}
function navigate(view) {
  state.view = view;
  $$(".nav-item").forEach((button) => button.classList.toggle("active", button.dataset.view === view));
  const title = { overview: "Visão geral", orders: "Ordens de serviço", people: "Clientes e veículos", stock: "Estoque", team: "Equipe", api: "Explorador de API" }[view];
  $("#page-title").textContent = title;
  const template = $(`#${view}-template`);
  $("#view-container").replaceChildren(template.content.cloneNode(true));
  ({ overview: loadOverview, orders: loadOrders, people: loadPeople, stock: loadStock, team: loadTeam, api: loadApi }[view])();
}

async function loadOverview() {
  try {
    const [orders, clients, parts] = await Promise.all([request("/api/v1/ordensdeservico/abertas"), request("/api/v1/clientes"), request("/api/v1/pecas")]);
    state.orders = orders || []; state.clients = clients || []; state.parts = parts || [];
    $("#metric-open").textContent = state.orders.length; $("#metric-clients").textContent = state.clients.length; $("#metric-parts").textContent = state.parts.length;
    try {
      const metric = await request("/api/v1/ordensdeservico/metricas/30");
      $("#metric-time").textContent = `${Math.round(Number(metric.tempoMs || 0) / 86400000 * 10) / 10}d`;
    } catch { $("#metric-time").textContent = "—"; }
    $("#overview-orders").innerHTML = state.orders.length ? state.orders.slice(0, 5).map(orderRow).join("") : empty("Nenhuma ordem aberta no momento.");
  } catch (error) { toast(error.message, true); }
}
function orderRow(order) {
  return `<div class="list-row"><div><strong>OS #${esc(order.id)}</strong><small>${esc(order.placaVeiculo)} · ${esc(order.documentoCliente)} · criada em ${date(order.dataCriacao)}</small></div><span class="badge ${order.status === "FINALIZADA" ? "gray" : "orange"}">${esc(order.status || "ABERTA")}</span></div>`;
}
function empty(message) { return `<div class="empty">${esc(message)}</div>`; }

async function loadOrders() {
  try { state.orders = await request(`/api/v1/ordensdeservico${state.orderFilter === "open" ? "/abertas" : ""}`) || []; renderOrders(); }
  catch (error) { toast(error.message, true); }
}
function renderOrders() {
  const rows = state.orders.map((order) => `<tr><td>OS #${esc(order.id)}</td><td>${esc(order.documentoCliente)}</td><td>${esc(order.placaVeiculo)}</td><td>${date(order.dataCriacao)}</td><td><span class="badge ${order.status === "FINALIZADA" || order.status === "ENTREGUE" ? "gray" : "orange"}">${esc(order.status)}</span></td><td>${money(order.orcamento)}</td><td><div class="table-actions"><button class="table-action" data-order-id="${order.id}" data-action="view-order">Ver</button><button class="table-action" data-order-id="${order.id}" data-action="advance-order">Avançar</button></div></td></tr>`).join("");
  $("#orders-table").innerHTML = rows ? `<table class="data-table"><thead><tr><th>Ordem</th><th>Cliente</th><th>Veículo</th><th>Criação</th><th>Status</th><th>Orçamento</th><th>Ações</th></tr></thead><tbody>${rows}</tbody></table>` : empty("Nenhuma ordem encontrada.");
}
function orderForm() {
  $("#order-form-panel").classList.remove("hidden");
  $("#order-form-panel").innerHTML = `<div class="panel-heading"><div><span class="eyebrow">NOVO ATENDIMENTO</span><h3>Abrir ordem de serviço</h3></div><button class="text-button" data-action="close-form">Fechar ×</button></div><form id="order-form" class="form-grid"><label>Documento do cliente<input name="documentoCliente" placeholder="CPF ou CNPJ" required></label><label>Placa do veículo<input name="placaVeiculo" placeholder="ABC1D23" required></label><label class="full">Queixa / descrição<textarea name="descricaoQueixas" rows="3" required placeholder="Descreva o problema relatado pelo cliente"></textarea></label><div class="form-actions full"><button type="button" class="button button-ghost" data-action="close-form">Cancelar</button><button class="button button-primary">Criar ordem <span>→</span></button></div></form>`;
}
async function submitOrder(form) {
  try { const data = Object.fromEntries(new FormData(form)); await request("/api/v1/ordensdeservico", { method: "POST", body: JSON.stringify(data) }); toast(`OS #${data.id || "criada"} aberta com sucesso`); await loadOrders(); $("#order-form-panel").classList.add("hidden"); }
  catch (error) { toast(error.message, true); }
}
async function showOrder(id) {
  try {
    const order = await request(`/api/v1/ordensdeservico/${id}`);
    $("#order-detail-panel").classList.remove("hidden");
    $("#order-detail-panel").innerHTML = `<div class="panel-heading"><div><span class="eyebrow">DETALHES DA OS</span><h3>Ordem #${esc(order.id)} <span class="badge">${esc(order.status)}</span></h3></div><button class="text-button" data-action="close-detail">Fechar ×</button></div><div class="detail-grid"><div><small>Cliente</small><strong>${esc(order.documentoCliente)}</strong></div><div><small>Veículo</small><strong>${esc(order.placaVeiculo)}</strong></div><div><small>Orçamento</small><strong>${money(order.orcamento)}</strong></div><div><small>Criada em</small><strong>${date(order.dataCriacao)}</strong></div><div><small>Queixa</small><strong>${esc(order.descricaoQueixas)}</strong></div><div><small>Diagnóstico</small><strong>${esc(order.diagnosticos) || "Ainda não informado"}</strong></div><div><small>Serviços</small><strong>${(order.servicos || []).map((item) => esc(item.descricao)).join(", ") || "Nenhum"}</strong></div><div><small>Peças</small><strong>${(order.pecasNecessarias || []).length || 0} item(ns)</strong></div></div>`;
  } catch (error) { toast(error.message, true); }
}
async function advanceOrder(id) {
  const order = state.orders.find((item) => String(item.id) === String(id)); if (!order) return;
  const next = { ABERTA: ["iniciar-diagnostico", "Iniciar diagnóstico"], EM_DIAGNOSTICO: ["enviar-orcamento", "Enviar orçamento"], AGUARDANDO_APROVACAO: ["aprovar-orcamento", "Aprovar orçamento"], APROVADA: ["finalizar-reparo", "Finalizar reparo"], FINALIZADA: ["entregar-veiculo", "Entregar veículo"] }[order.status];
  if (!next) { toast("Esta ordem não possui uma próxima etapa automática.", true); return; }
  let body; if (next[0] === "aprovar-orcamento") body = { aprovado: confirm("O cliente aprovou o orçamento?") };
  try { await request(`/api/v1/ordensdeservico/${id}/${next[0]}`, { method: "PATCH", ...(body ? { body: JSON.stringify(body) } : {}) }); toast(next[1] + " realizado."); await loadOrders(); }
  catch (error) { toast(error.message, true); }
}

async function loadPeople() { await (state.peopleTab === "clients" ? loadClients() : loadVehicles()); }
async function loadClients() {
  try { state.clients = await request("/api/v1/clientes") || []; renderPeople(); } catch (error) { toast(error.message, true); }
}
async function loadVehicles() {
  try { state.vehicles = await request("/api/v1/veiculos") || []; renderPeople(); } catch (error) { toast(error.message, true); }
}
function renderPeople() {
  const isClients = state.peopleTab === "clients"; const data = isClients ? state.clients : state.vehicles;
  const rows = data.map((item) => isClients ? `<tr><td>${esc(item.nome)}</td><td>${esc(item.documento)}</td><td>${esc(item.email)}</td><td>${esc(item.telefone)}</td><td><button class="table-action danger" data-action="delete-client" data-id="${esc(item.documento)}">Excluir</button></td></tr>` : `<tr><td>${esc(item.placa)}</td><td>${esc(item.marca)}</td><td>${esc(item.modelo)}</td><td>${esc(item.ano)}</td><td><button class="table-action danger" data-action="delete-vehicle" data-id="${esc(item.placa)}">Excluir</button></td></tr>`).join("");
  $("#people-table").innerHTML = `<table class="data-table"><thead><tr>${isClients ? "<th>Nome</th><th>Documento</th><th>E-mail</th><th>Telefone</th>" : "<th>Placa</th><th>Marca</th><th>Modelo</th><th>Ano</th>"}<th>Ações</th></tr></thead><tbody>${rows}</tbody></table>`;
}
function personForm() {
  const client = state.peopleTab === "clients";
  $("#people-form-panel").classList.remove("hidden");
  $("#people-form-panel").innerHTML = `<div class="panel-heading"><div><span class="eyebrow">NOVO CADASTRO</span><h3>${client ? "Cadastrar cliente" : "Cadastrar veículo"}</h3></div><button class="text-button" data-action="close-form">Fechar ×</button></div><form id="person-form" class="form-grid">${client ? '<label>Documento<input name="documento" required></label><label>Nome<input name="nome" required></label><label>E-mail<input type="email" name="email" required></label><label>Telefone<input name="telefone" required></label>' : '<label>Placa<input name="placa" required></label><label>Marca<input name="marca" required></label><label>Modelo<input name="modelo" required></label><label>Ano<input type="number" name="ano" required></label>'}<div class="form-actions full"><button type="button" class="button button-ghost" data-action="close-form">Cancelar</button><button class="button button-primary">Salvar <span>→</span></button></div></form>`;
}
async function submitPerson(form) {
  const client = state.peopleTab === "clients"; const data = Object.fromEntries(new FormData(form));
  try { await request(client ? "/api/v1/clientes" : "/api/v1/veiculos", { method: "POST", body: JSON.stringify(data) }); toast("Cadastro criado com sucesso."); $("#people-form-panel").classList.add("hidden"); await loadPeople(); }
  catch (error) { toast(error.message, true); }
}

async function loadStock() { await (state.stockTab === "parts" ? loadParts() : loadServices()); }
async function loadParts() { try { state.parts = await request("/api/v1/pecas") || []; renderStock(); } catch (error) { toast(error.message, true); } }
async function loadServices() { try { state.services = await request("/api/v1/servicos") || []; renderStock(); } catch (error) { toast(error.message, true); } }
function renderStock() {
  const parts = state.stockTab === "parts"; const data = parts ? state.parts : state.services;
  const rows = data.map((item) => parts ? `<tr><td>#${esc(item.id)}</td><td>${esc(item.descricao)}</td><td>${money(item.valor)}</td><td>${esc(item.quantidade)}</td><td><div class="table-actions"><button class="table-action" data-action="restock" data-id="${item.id}">Repor</button><button class="table-action danger" data-action="delete-part" data-id="${item.id}">Excluir</button></div></td></tr>` : `<tr><td>#${esc(item.id)}</td><td>${esc(item.descricao)}</td><td>${money(item.valor)}</td><td><button class="table-action danger" data-action="delete-service" data-id="${item.id}">Excluir</button></td></tr>`).join("");
  $("#stock-table").innerHTML = `<table class="data-table"><thead><tr>${parts ? "<th>ID</th><th>Descrição</th><th>Valor</th><th>Quantidade</th><th>Ações</th>" : "<th>ID</th><th>Descrição</th><th>Valor</th><th>Ações</th>"}</tr></thead><tbody>${rows}</tbody></table>`;
}
function stockForm() {
  const part = state.stockTab === "parts"; $("#stock-form-panel").classList.remove("hidden");
  $("#stock-form-panel").innerHTML = `<div class="panel-heading"><div><span class="eyebrow">INVENTÁRIO</span><h3>${part ? "Adicionar peça" : "Cadastrar serviço"}</h3></div><button class="text-button" data-action="close-form">Fechar ×</button></div><form id="stock-form" class="form-grid"><label>Descrição<input name="descricao" required></label><label>Valor<input type="number" step="0.01" name="valor" required></label>${part ? '<label>Quantidade<input type="number" min="0" name="quantidade" required></label>' : ""}<div class="form-actions full"><button type="button" class="button button-ghost" data-action="close-form">Cancelar</button><button class="button button-primary">Salvar <span>→</span></button></div></form>`;
}
async function submitStock(form) {
  const part = state.stockTab === "parts"; const data = Object.fromEntries(new FormData(form)); data.valor = Number(data.valor); if (part) data.quantidade = Number(data.quantidade);
  try { await request(part ? "/api/v1/pecas/criar" : "/api/v1/servicos", { method: "POST", body: JSON.stringify(data) }); toast("Item salvo com sucesso."); $("#stock-form-panel").classList.add("hidden"); await loadStock(); } catch (error) { toast(error.message, true); }
}

async function loadTeam() { try { state.employees = await request("/api/v1/administrativo/funcionarios") || []; renderTeam(); } catch (error) { toast(error.message, true); } }
function renderTeam() {
  const rows = state.employees.map((item) => `<tr><td>${esc(item.nome)}</td><td>${esc(item.email)}</td><td>${esc(item.perfil)}</td><td><span class="badge ${item.ativo === false ? "gray" : ""}">${item.ativo === false ? "Inativo" : "Ativo"}</span></td><td><div class="table-actions"><button class="table-action" data-action="toggle-employee" data-id="${item.id}">${item.ativo === false ? "Ativar" : "Inativar"}</button></div></td></tr>`).join("");
  $("#team-table").innerHTML = `<table class="data-table"><thead><tr><th>Nome</th><th>E-mail</th><th>Perfil</th><th>Status</th><th>Ações</th></tr></thead><tbody>${rows}</tbody></table>`;
}
function employeeForm() {
  $("#team-form-panel").classList.remove("hidden"); $("#team-form-panel").innerHTML = `<div class="panel-heading"><div><span class="eyebrow">NOVO ACESSO</span><h3>Cadastrar funcionário</h3></div><button class="text-button" data-action="close-form">Fechar ×</button></div><form id="employee-form" class="form-grid"><label>Nome<input name="nome" required></label><label>E-mail<input type="email" name="email" required></label><label>Senha<input type="password" name="senha" required></label><label>Perfil<select name="perfil"><option value="ADMINISTRADOR">Administrador</option><option value="FUNCIONARIO">Funcionário</option></select></label><div class="form-actions full"><button type="button" class="button button-ghost" data-action="close-form">Cancelar</button><button class="button button-primary">Criar acesso <span>→</span></button></div></form>`;
}
async function submitEmployee(form) { try { await request("/api/v1/administrativo/funcionarios", { method: "POST", body: JSON.stringify(Object.fromEntries(new FormData(form))) }); toast("Funcionário criado."); $("#team-form-panel").classList.add("hidden"); await loadTeam(); } catch (error) { toast(error.message, true); } }

function loadApi() {}
async function executeApi(form) {
  const data = Object.fromEntries(new FormData(form)); let body;
  try { body = data.body.trim() ? JSON.parse(data.body) : undefined; } catch { toast("O corpo precisa ser um JSON válido.", true); return; }
  try {
    const response = await request(data.path, { method: data.method, ...(body !== undefined ? { body: JSON.stringify(body) } : {}) });
    $("#api-status").textContent = "200 · Sucesso"; $("#api-output").textContent = JSON.stringify(response, null, 2);
  } catch (error) { $("#api-status").textContent = "Erro na requisição"; $("#api-output").textContent = error.message; }
}

document.addEventListener("submit", (event) => {
  event.preventDefault();
  if (event.target.id === "login-form") login(event.target);
  if (event.target.id === "order-form") submitOrder(event.target);
  if (event.target.id === "person-form") submitPerson(event.target);
  if (event.target.id === "stock-form") submitStock(event.target);
  if (event.target.id === "employee-form") submitEmployee(event.target);
  if (event.target.id === "api-form") executeApi(event.target);
});
document.addEventListener("click", async (event) => {
  const button = event.target.closest("button"); if (!button) return;
  if (button.dataset.view) navigate(button.dataset.view);
  if (button.dataset.go) navigate(button.dataset.go);
  const action = button.dataset.action;
  if (action === "new-order") orderForm();
  if (action === "new-client") { state.peopleTab = "clients"; navigate("people"); window.setTimeout(personForm, 0); }
  if (action === "new-part") { state.stockTab = "parts"; navigate("stock"); window.setTimeout(stockForm, 0); }
  if (action === "new-employee") employeeForm();
  if (action === "close-form") $$(".form-panel").forEach((panel) => panel.classList.add("hidden"));
  if (action === "close-detail") $("#order-detail-panel").classList.add("hidden");
  if (action === "view-order") showOrder(button.dataset.orderId);
  if (action === "advance-order") advanceOrder(button.dataset.orderId);
  if (action === "refresh-orders") loadOrders();
  if (action === "delete-client" && confirm("Excluir este cliente?")) { try { await request(`/api/v1/clientes/${button.dataset.id}`, { method: "DELETE" }); toast("Cliente removido."); loadClients(); } catch (error) { toast(error.message, true); } }
  if (action === "delete-vehicle" && confirm("Excluir este veículo?")) { try { await request(`/api/v1/veiculos/${button.dataset.id}`, { method: "DELETE" }); toast("Veículo removido."); loadVehicles(); } catch (error) { toast(error.message, true); } }
  if (action === "delete-part" && confirm("Excluir esta peça?")) { try { await request(`/api/v1/pecas/${button.dataset.id}`, { method: "DELETE" }); toast("Peça removida."); loadParts(); } catch (error) { toast(error.message, true); } }
  if (action === "delete-service" && confirm("Excluir este serviço?")) { try { await request(`/api/v1/servicos/${button.dataset.id}`, { method: "DELETE" }); toast("Serviço removido."); loadServices(); } catch (error) { toast(error.message, true); } }
  if (action === "restock") { const quantity = Number(prompt("Quantidade para repor:")); if (quantity > 0) { try { await request("/api/v1/pecas/repor", { method: "PUT", body: JSON.stringify({ id: Number(button.dataset.id), quantidade: quantity }) }); toast("Estoque atualizado."); loadParts(); } catch (error) { toast(error.message, true); } } }
  if (action === "toggle-employee") { const employee = state.employees.find((item) => String(item.id) === button.dataset.id); const verb = employee?.ativo === false ? "ativar" : "inativar"; try { await request(`/api/v1/administrativo/funcionarios/${button.dataset.id}/${verb}`, { method: "PATCH" }); toast(`Funcionário ${verb === "ativar" ? "ativado" : "inativado"}.`); loadTeam(); } catch (error) { toast(error.message, true); } }
  if (button.dataset.orderFilter) { state.orderFilter = button.dataset.orderFilter; $$(".segmented button").forEach((item) => item.classList.toggle("active", item === button)); loadOrders(); }
  if (button.dataset.peopleTab) { state.peopleTab = button.dataset.peopleTab; $$(".tabs button").forEach((item) => item.classList.toggle("active", item.dataset.peopleTab === state.peopleTab)); loadPeople(); }
  if (button.dataset.stockTab) { state.stockTab = button.dataset.stockTab; $$(".tabs button").forEach((item) => item.classList.toggle("active", item.dataset.stockTab === state.stockTab)); loadStock(); }
  if (button.id === "logout-button") logout();
  if (button.id === "mobile-menu") $(".sidebar").classList.toggle("open");
  if (button.id === "copy-api-result") navigator.clipboard?.writeText($("#api-output").textContent);
});
async function login(form) {
  try {
    const data = Object.fromEntries(new FormData(form)); const result = await request("/api/v1/administrativo/autenticacao", { method: "POST", body: JSON.stringify(data) });
    state.token = result.token || result.jwt || result.accessToken; state.email = data.email;
    if (!state.token) throw new Error("A API não retornou um token JWT.");
    sessionStorage.setItem("oficina_token", state.token); sessionStorage.setItem("oficina_email", state.email); showApp();
  } catch (error) { toast(error.message, true); }
}
if (state.token) showApp();

function esc(value) {
    return String(value ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
}
function pesos(value) { return new Intl.NumberFormat('es-CO', {style:'currency',currency:'COP',maximumFractionDigits:0}).format(value ?? 0); }
function estado(id, texto, error = false) {
    const el = document.getElementById(id);
    el.textContent = texto;
    el.className = error ? 'status error' : 'status success';
}
async function api(url, options = {}) {
    let response;
    try { response = await fetch(url, options); }
    catch { throw new Error('No se pudo conectar con el programa. Comprueba que siga abierto en el computador.'); }
    if (!response.ok) {
        const detail = await response.json().catch(() => ({}));
        throw new Error(detail.detail || detail.message || 'No se pudo completar la operación. Revisa los datos.');
    }
    return response.json();
}
function jsonRequest(method, body) {
    return {method, headers: {'Content-Type': 'application/json'}, body: JSON.stringify(body)};
}
function jsonPost(body) { return jsonRequest('POST', body); }

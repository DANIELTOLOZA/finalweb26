/**
 * PokéManager SPA Main Script
 * Handles view switching, dynamic CRUD modals, API data synchronization, and capture simulation.
 */

// Application State
const state = {
    currentView: 'dashboard',
    pueblos: [],
    tipos: [],
    pokemons: [],
    entrenadores: [],
    capturas: [],
    activeEditingId: null // Stores the ID of the resource being edited
};

// API Endpoints
const API = {
    pueblos: '/api/pueblos',
    tipos: '/api/tipos',
    pokemons: '/api/pokemons',
    entrenadores: '/api/entrenadores',
    capturas: '/api/capturas'
};

// DOM Elements
const elements = {
    menuItems: document.querySelectorAll('.menu-item'),
    viewSections: document.querySelectorAll('.view-section'),
    sectionTitle: document.getElementById('section-title'),
    sectionSubtitle: document.getElementById('section-subtitle'),
    
    // Stats
    statPokemons: document.getElementById('stat-pokemons'),
    statEntrenadores: document.getElementById('stat-entrenadores'),
    statPueblos: document.getElementById('stat-pueblos'),
    statCapturas: document.getElementById('stat-capturas'),
    
    // Forms & Simulator
    formSimulator: document.getElementById('form-simulator'),
    simEntrenador: document.getElementById('sim-entrenador'),
    simPokemon: document.getElementById('sim-pokemon'),
    btnSimulateCapture: document.getElementById('btn-simulate-capture'),
    ballWrapper: document.getElementById('ball-wrapper'),
    pokeballElement: document.getElementById('pokeball-element'),
    pokeballButton: document.getElementById('pokeball-button'),
    captureStatusText: document.getElementById('capture-status-text'),
    
    // Tables
    tableRecentCaptures: document.querySelector('#table-recent-captures tbody'),
    tableEntrenadores: document.querySelector('#table-entrenadores tbody'),
    tablePokemons: document.querySelector('#table-pokemons tbody'),
    tablePueblos: document.querySelector('#table-pueblos tbody'),
    tableTipos: document.querySelector('#table-tipos tbody'),
    tableCapturas: document.querySelector('#table-capturas tbody'),
    
    // Modals
    modalFormOverlay: document.getElementById('modal-form-overlay'),
    modalFormTitle: document.getElementById('modal-form-title'),
    modalFormClose: document.getElementById('modal-form-close'),
    formFields: document.getElementById('form-fields'),
    activeForm: document.getElementById('active-form'),
    btnCancelForm: document.getElementById('btn-cancel-form'),
    btnQuickCapture: document.getElementById('btn-quick-capture'),
    toastContainer: document.getElementById('toast-container')
};

// --- INITIALIZATION ---
document.addEventListener('DOMContentLoaded', () => {
    initNavigation();
    initEventHandlers();
    refreshAllData();
});

// --- NAVIGATION LOGIC ---
function initNavigation() {
    elements.menuItems.forEach(item => {
        item.addEventListener('click', () => {
            const target = item.getAttribute('data-target');
            switchView(target);
        });
    });

    elements.btnQuickCapture.addEventListener('click', () => {
        switchView('dashboard');
        elements.simEntrenador.focus();
    });
}

function switchView(viewName) {
    state.currentView = viewName;
    
    // Update menu buttons
    elements.menuItems.forEach(btn => {
        if (btn.getAttribute('data-target') === viewName) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });

    // Update sections
    elements.viewSections.forEach(section => {
        if (section.id === `view-${viewName}`) {
            section.classList.add('active');
        } else {
            section.classList.remove('active');
        }
    });

    // Update headers text
    const viewMetadata = {
        dashboard: { title: 'Dashboard', subtitle: 'Vista general de tu base de datos Pokémon' },
        entrenadores: { title: 'Entrenadores', subtitle: 'Registra y edita los entrenadores de la liga' },
        pokemons: { title: 'Pokémon', subtitle: 'Colección de especies registradas en el Pokédex' },
        pueblos: { title: 'Pueblos', subtitle: 'Ubicaciones geográficas del mundo Pokémon' },
        tipos: { title: 'Tipos Pokémon', subtitle: 'Clasificación elemental de las especies' },
        capturas: { title: 'Historial de Capturas', subtitle: 'Registro histórico de Pokémon atrapados' }
    };

    const meta = viewMetadata[viewName] || { title: viewName.toUpperCase(), subtitle: '' };
    elements.sectionTitle.textContent = meta.title;
    elements.sectionSubtitle.textContent = meta.subtitle;

    // Refresh view data specifically
    refreshViewData(viewName);
}

// --- DATA FETCHING & SYNCHRONIZATION ---
async function refreshAllData() {
    try {
        await Promise.all([
            fetchPueblos(),
            fetchTipos(),
            fetchPokemons(),
            fetchEntrenadores(),
            fetchCapturas()
        ]);
        
        updateDashboardStats();
        populateSimulatorDropdowns();
        populateRecentCapturesTable();
    } catch (err) {
        showToast('Error al conectar con la base de datos', 'error');
        console.error(err);
    }
}

async function refreshViewData(viewName) {
    await refreshAllData();
    
    if (viewName === 'entrenadores') populateEntrenadoresTable();
    else if (viewName === 'pokemons') populatePokemonsTable();
    else if (viewName === 'pueblos') populatePueblosTable();
    else if (viewName === 'tipos') populateTiposTable();
    else if (viewName === 'capturas') populateCapturasTable();
}

async function fetchPueblos() {
    const res = await fetch(API.pueblos);
    state.pueblos = await res.json();
}

async function fetchTipos() {
    const res = await fetch(API.tipos);
    state.tipos = await res.json();
}

async function fetchPokemons() {
    const res = await fetch(API.pokemons);
    state.pokemons = await res.json();
}

async function fetchEntrenadores() {
    const res = await fetch(API.entrenadores);
    state.entrenadores = await res.json();
}

async function fetchCapturas() {
    const res = await fetch(API.capturas);
    state.capturas = await res.json();
}

// --- DASHBOARD RENDER ---
function updateDashboardStats() {
    elements.statPokemons.textContent = state.pokemons.length;
    elements.statEntrenadores.textContent = state.entrenadores.length;
    elements.statPueblos.textContent = state.pueblos.length;
    elements.statCapturas.textContent = state.capturas.length;
}

function populateSimulatorDropdowns() {
    // Populate Trainers
    elements.simEntrenador.innerHTML = '<option value="">Selecciona un Entrenador...</option>';
    state.entrenadores.forEach(e => {
        elements.simEntrenador.innerHTML += `<option value="${e.id}">${e.nombre} ${e.apellido}</option>`;
    });

    // Populate Pokemon (only ones not captured, or all. Let's make it more fun: only pokemons that are not currently captured!)
    const capturedIds = state.capturas.map(c => c.pokemon.id);
    const freePokemons = state.pokemons.filter(p => !capturedIds.includes(p.id));

    elements.simPokemon.innerHTML = '<option value="">Selecciona un Pokémon...</option>';
    freePokemons.forEach(p => {
        elements.simPokemon.innerHTML += `<option value="${p.id}">${p.nombre} (Gen ${p.generacion} - ${p.tipoPokemon.descripcion})</option>`;
    });
}

function populateRecentCapturesTable() {
    elements.tableRecentCaptures.innerHTML = '';
    // Sort captures by registration order, showing latest first (slice to take last 5)
    const recent = state.capturas.slice(-5).reverse();
    
    if (recent.length === 0) {
        elements.tableRecentCaptures.innerHTML = '<tr><td colspan="5" style="text-align: center;">No hay capturas registradas.</td></tr>';
        return;
    }

    recent.forEach(c => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><strong>${c.entrenador.nombre} ${c.entrenador.apellido}</strong></td>
            <td>${c.pokemon.nombre}</td>
            <td><span class="badge badge-tipo">${c.pokemon.tipoPokemon.descripcion}</span></td>
            <td><code class="uuid-text">${c.pokemon.uuid.substring(0, 13)}...</code></td>
            <td>
                <button class="btn btn-danger btn-icon-only" onclick="liberarPokemon(${c.pokemon.id}, ${c.entrenador.id})">
                    <i class="fa-solid fa-cookie-bite"></i> Liberar
                </button>
            </td>
        `;
        elements.tableRecentCaptures.appendChild(tr);
    });
}

// --- RESOURCE TABLES RENDER ---
function populateEntrenadoresTable() {
    elements.tableEntrenadores.innerHTML = '';
    if (state.entrenadores.length === 0) {
        elements.tableEntrenadores.innerHTML = '<tr><td colspan="8" style="text-align: center;">No hay entrenadores registrados.</td></tr>';
        return;
    }
    state.entrenadores.forEach(e => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${e.id}</td>
            <td><strong>${e.nombre}</strong></td>
            <td>${e.apellido}</td>
            <td>${e.fechaNacimiento}</td>
            <td>${e.fechaVinculacion}</td>
            <td>${e.pueblo ? e.pueblo.nombre : 'Sin pueblo'}</td>
            <td><code class="uuid-text">${e.uuid}</code></td>
            <td>
                <button class="btn btn-secondary btn-icon-only" onclick="openEditModal('entrenadores', ${e.id})" title="Editar"><i class="fa-solid fa-pen"></i></button>
                <button class="btn btn-danger btn-icon-only" onclick="deleteResource('entrenadores', ${e.id})" title="Eliminar"><i class="fa-solid fa-trash"></i></button>
            </td>
        `;
        elements.tableEntrenadores.appendChild(tr);
    });
}

function populatePokemonsTable() {
    elements.tablePokemons.innerHTML = '';
    if (state.pokemons.length === 0) {
        elements.tablePokemons.innerHTML = '<tr><td colspan="8" style="text-align: center;">No hay Pokémon registrados.</td></tr>';
        return;
    }
    state.pokemons.forEach(p => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${p.id}</td>
            <td><strong>${p.nombre}</strong></td>
            <td><span class="badge badge-tipo">${p.tipoPokemon ? p.tipoPokemon.descripcion : 'Sin tipo'}</span></td>
            <td><span class="badge badge-generacion">G${p.generacion}</span></td>
            <td>${p.fechaDescubrimiento}</td>
            <td><span class="desc-cell" title="${p.descripcion}">${p.descripcion.substring(0, 50)}${p.descripcion.length > 50 ? '...' : ''}</span></td>
            <td><code class="uuid-text">${p.uuid}</code></td>
            <td>
                <button class="btn btn-secondary btn-icon-only" onclick="openEditModal('pokemons', ${p.id})" title="Editar"><i class="fa-solid fa-pen"></i></button>
                <button class="btn btn-danger btn-icon-only" onclick="deleteResource('pokemons', ${p.id})" title="Eliminar"><i class="fa-solid fa-trash"></i></button>
            </td>
        `;
        elements.tablePokemons.appendChild(tr);
    });
}

function populatePueblosTable() {
    elements.tablePueblos.innerHTML = '';
    if (state.pueblos.length === 0) {
        elements.tablePueblos.innerHTML = '<tr><td colspan="4" style="text-align: center;">No hay pueblos registrados.</td></tr>';
        return;
    }
    state.pueblos.forEach(p => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${p.id}</td>
            <td><strong>${p.nombre}</strong></td>
            <td><code class="uuid-text">${p.uuid}</code></td>
            <td>
                <button class="btn btn-secondary btn-icon-only" onclick="openEditModal('pueblos', ${p.id})" title="Editar"><i class="fa-solid fa-pen"></i></button>
                <button class="btn btn-danger btn-icon-only" onclick="deleteResource('pueblos', ${p.id})" title="Eliminar"><i class="fa-solid fa-trash"></i></button>
            </td>
        `;
        elements.tablePueblos.appendChild(tr);
    });
}

function populateTiposTable() {
    elements.tableTipos.innerHTML = '';
    if (state.tipos.length === 0) {
        elements.tableTipos.innerHTML = '<tr><td colspan="4" style="text-align: center;">No hay tipos registrados.</td></tr>';
        return;
    }
    state.tipos.forEach(t => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${t.id}</td>
            <td><strong>${t.descripcion}</strong></td>
            <td><code class="uuid-text">${t.uuid}</code></td>
            <td>
                <button class="btn btn-secondary btn-icon-only" onclick="openEditModal('tipos', ${t.id})" title="Editar"><i class="fa-solid fa-pen"></i></button>
                <button class="btn btn-danger btn-icon-only" onclick="deleteResource('tipos', ${t.id})" title="Eliminar"><i class="fa-solid fa-trash"></i></button>
            </td>
        `;
        elements.tableTipos.appendChild(tr);
    });
}

function populateCapturasTable() {
    elements.tableCapturas.innerHTML = '';
    if (state.capturas.length === 0) {
        elements.tableCapturas.innerHTML = '<tr><td colspan="5" style="text-align: center;">No hay capturas registradas.</td></tr>';
        return;
    }
    state.capturas.forEach(c => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><strong>${c.entrenador.nombre} ${c.entrenador.apellido}</strong></td>
            <td>${c.pokemon.nombre}</td>
            <td><span class="badge badge-tipo">${c.pokemon.tipoPokemon.descripcion}</span></td>
            <td><span class="badge badge-generacion">Generación ${c.pokemon.generacion}</span></td>
            <td>
                <button class="btn btn-danger" onclick="liberarPokemon(${c.pokemon.id}, ${c.entrenador.id})">
                    <i class="fa-solid fa-cookie-bite"></i> Liberar Pokémon
                </button>
            </td>
        `;
        elements.tableCapturas.appendChild(tr);
    });
}

// --- CAPTURE SIMULATION LOGIC ---
function initEventHandlers() {
    // Simulator Submit
    elements.formSimulator.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const pokemonId = parseInt(elements.simPokemon.value);
        const entrenadorId = parseInt(elements.simEntrenador.value);
        
        if (!pokemonId || !entrenadorId) return;
        
        // Block button
        elements.btnSimulateCapture.disabled = true;
        
        // Reset elements to normal state
        elements.ballWrapper.className = 'ball-wrapper';
        elements.pokeballButton.className = 'pokeball-button';
        elements.captureStatusText.textContent = '¡Lanzando Poké Ball!';
        
        // Trigger CSS animations
        setTimeout(() => {
            elements.ballWrapper.classList.add('ball-throw');
        }, 50);

        setTimeout(() => {
            elements.ballWrapper.classList.add('ball-shake');
            elements.pokeballButton.classList.add('ball-glow-red');
            elements.captureStatusText.textContent = '¡Atrápalo... wingle wangle!';
        }, 1200);

        // Perform REST call in parallel
        try {
            const response = await fetch(API.capturas, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ pokemonId, entrenadorId })
            });
            
            const data = await response.json();
            
            // Wait for simulator timing to end (2.8s total) before showing success
            setTimeout(() => {
                if (response.ok) {
                    // Success state
                    elements.ballWrapper.classList.remove('ball-shake');
                    elements.pokeballButton.classList.remove('ball-glow-red');
                    elements.pokeballButton.classList.add('ball-glow-green');
                    elements.captureStatusText.textContent = '¡CAPTURADO CON ÉXITO!';
                    showToast(`¡${data.pokemon.nombre} capturado por ${data.entrenador.nombre}!`, 'success');
                    
                    refreshViewData(state.currentView);
                } else {
                    // Fail state
                    resetSimulator();
                    showToast(data.error || 'Fallo en la captura', 'error');
                }
                elements.btnSimulateCapture.disabled = false;
            }, 3000);
            
        } catch (err) {
            setTimeout(() => {
                resetSimulator();
                showToast('Error en la llamada de red', 'error');
                elements.btnSimulateCapture.disabled = false;
            }, 3000);
        }
    });

    // Close Modals
    elements.modalFormClose.addEventListener('click', closeModal);
    elements.btnCancelForm.addEventListener('click', closeModal);
    
    // Add Buttons listeners
    document.getElementById('btn-add-pueblo').addEventListener('click', () => openCreateModal('pueblos'));
    document.getElementById('btn-add-tipo').addEventListener('click', () => openCreateModal('tipos'));
    document.getElementById('btn-add-pokemon').addEventListener('click', () => openCreateModal('pokemons'));
    document.getElementById('btn-add-entrenador').addEventListener('click', () => openCreateModal('entrenadores'));
    
    // Form Submit
    elements.activeForm.addEventListener('submit', handleFormSubmit);
}

function resetSimulator() {
    elements.ballWrapper.className = 'ball-wrapper';
    elements.pokeballButton.className = 'pokeball-button';
    elements.captureStatusText.textContent = '¡Oh no! Se ha escapado.';
}

// --- CRUD ACTION MODALS ---
function openCreateModal(resourceType) {
    state.activeEditingId = null;
    buildFormFields(resourceType);
    
    const titles = {
        pueblos: 'Descubrir Nuevo Pueblo',
        tipos: 'Crear Tipo Elemental',
        pokemons: 'Registrar Nueva Especie Pokémon',
        entrenadores: 'Inscribir Entrenador de la Liga'
    };
    
    elements.modalFormTitle.textContent = titles[resourceType] || 'Añadir Registro';
    elements.modalFormOverlay.setAttribute('data-resource', resourceType);
    elements.modalFormOverlay.classList.add('active');
}

function openEditModal(resourceType, id) {
    state.activeEditingId = id;
    buildFormFields(resourceType);
    
    // Find active element
    let element = null;
    if (resourceType === 'pueblos') element = state.pueblos.find(p => p.id === id);
    else if (resourceType === 'tipos') element = state.tipos.find(t => t.id === id);
    else if (resourceType === 'pokemons') element = state.pokemons.find(p => p.id === id);
    else if (resourceType === 'entrenadores') element = state.entrenadores.find(e => e.id === id);
    
    if (!element) return;
    
    // Fill values
    const form = elements.activeForm;
    
    if (resourceType === 'pueblos') {
        form.querySelector('#pueblo-nombre').value = element.nombre;
    } else if (resourceType === 'tipos') {
        form.querySelector('#tipo-descripcion').value = element.descripcion;
    } else if (resourceType === 'pokemons') {
        form.querySelector('#pk-nombre').value = element.nombre;
        form.querySelector('#pk-descripcion').value = element.descripcion;
        form.querySelector('#pk-tipo').value = element.tipoPokemon ? element.tipoPokemon.id : '';
        form.querySelector('#pk-fecha').value = element.fechaDescubrimiento;
        form.querySelector('#pk-generacion').value = element.generacion;
    } else if (resourceType === 'entrenadores') {
        form.querySelector('#tr-nombre').value = element.nombre;
        form.querySelector('#tr-apellido').value = element.apellido;
        form.querySelector('#tr-fechaNac').value = element.fechaNacimiento;
        form.querySelector('#tr-fechaVinc').value = element.fechaVinculacion;
        form.querySelector('#tr-pueblo').value = element.pueblo ? element.pueblo.id : '';
    }

    const titles = {
        pueblos: 'Editar Pueblo',
        tipos: 'Editar Tipo Elemental',
        pokemons: 'Editar Pokémon',
        entrenadores: 'Editar Entrenador'
    };
    
    elements.modalFormTitle.textContent = titles[resourceType] || 'Editar Registro';
    elements.modalFormOverlay.setAttribute('data-resource', resourceType);
    elements.modalFormOverlay.classList.add('active');
}

function buildFormFields(resourceType) {
    elements.formFields.innerHTML = '';
    
    if (resourceType === 'pueblos') {
        elements.formFields.innerHTML = `
            <div class="form-group">
                <label for="pueblo-nombre">Nombre del Pueblo / Ciudad</label>
                <input type="text" id="pueblo-nombre" required placeholder="Ej: Pueblo Paleta">
            </div>
        `;
    } else if (resourceType === 'tipos') {
        elements.formFields.innerHTML = `
            <div class="form-group">
                <label for="tipo-descripcion">Descripción del Tipo</label>
                <input type="text" id="tipo-descripcion" required placeholder="Ej: Fuego, Dragón, Acero">
            </div>
        `;
    } else if (resourceType === 'pokemons') {
        // Build types options list
        let typeOptions = state.tipos.map(t => `<option value="${t.id}">${t.descripcion}</option>`).join('');
        
        elements.formFields.innerHTML = `
            <div class="form-group">
                <label for="pk-nombre">Nombre del Pokémon</label>
                <input type="text" id="pk-nombre" required placeholder="Ej: Charizard">
            </div>
            <div class="form-group">
                <label for="pk-tipo">Tipo Principal</label>
                <select id="pk-tipo" required>
                    <option value="">Seleccione tipo...</option>
                    ${typeOptions}
                </select>
            </div>
            <div class="form-group">
                <label for="pk-generacion">Generación</label>
                <input type="number" id="pk-generacion" min="1" required placeholder="Ej: 1">
            </div>
            <div class="form-group">
                <label for="pk-fecha">Fecha de Descubrimiento</label>
                <input type="date" id="pk-fecha" required>
            </div>
            <div class="form-group">
                <label for="pk-descripcion">Descripción / Pokédex Entry</label>
                <textarea id="pk-descripcion" rows="3" required placeholder="Describe las habilidades o comportamiento del Pokémon..."></textarea>
            </div>
        `;
    } else if (resourceType === 'entrenadores') {
        // Build pueblos options list
        let puebloOptions = state.pueblos.map(p => `<option value="${p.id}">${p.nombre}</option>`).join('');
        
        elements.formFields.innerHTML = `
            <div class="form-group">
                <label for="tr-nombre">Nombre</label>
                <input type="text" id="tr-nombre" required placeholder="Ej: Red">
            </div>
            <div class="form-group">
                <label for="tr-apellido">Apellido</label>
                <input type="text" id="tr-apellido" required placeholder="Ej: Ketchum">
            </div>
            <div class="form-group">
                <label for="tr-pueblo">Pueblo de Origen</label>
                <select id="tr-pueblo" required>
                    <option value="">Seleccione pueblo...</option>
                    ${puebloOptions}
                </select>
            </div>
            <div class="form-group">
                <label for="tr-fechaNac">Fecha de Nacimiento</label>
                <input type="date" id="tr-fechaNac" required>
            </div>
            <div class="form-group">
                <label for="tr-fechaVinc">Fecha de Vinculación a la Liga</label>
                <input type="date" id="tr-fechaVinc" required>
            </div>
        `;
    }
}

function closeModal() {
    elements.modalFormOverlay.classList.remove('active');
    elements.activeForm.reset();
    state.activeEditingId = null;
}

// --- FORM SUBMISSION HANDLER ---
async function handleFormSubmit(e) {
    e.preventDefault();
    
    const resourceType = elements.modalFormOverlay.getAttribute('data-resource');
    let url = API[resourceType];
    let method = 'POST';
    
    if (state.activeEditingId) {
        url += `/${state.activeEditingId}`;
        method = 'PUT';
    }
    
    // Parse form values
    let bodyData = {};
    
    if (resourceType === 'pueblos') {
        bodyData = { nombre: document.getElementById('pueblo-nombre').value };
    } else if (resourceType === 'tipos') {
        bodyData = { descripcion: document.getElementById('tipo-descripcion').value };
    } else if (resourceType === 'pokemons') {
        bodyData = {
            nombre: document.getElementById('pk-nombre').value,
            descripcion: document.getElementById('pk-descripcion').value,
            generacion: parseInt(document.getElementById('pk-generacion').value),
            fechaDescubrimiento: document.getElementById('pk-fecha').value,
            tipoPokemon: { id: parseInt(document.getElementById('pk-tipo').value) }
        };
    } else if (resourceType === 'entrenadores') {
        bodyData = {
            nombre: document.getElementById('tr-nombre').value,
            apellido: document.getElementById('tr-apellido').value,
            fechaNacimiento: document.getElementById('tr-fechaNac').value,
            fechaVinculacion: document.getElementById('tr-fechaVinc').value,
            pueblo: { id: parseInt(document.getElementById('tr-pueblo').value) }
        };
    }
    
    try {
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bodyData)
        });
        
        if (response.ok) {
            showToast(`Registro guardado exitosamente.`, 'success');
            closeModal();
            refreshViewData(state.currentView);
        } else {
            const errData = await response.json();
            // Handle validation map or singular errors
            let errMsg = 'No se pudo guardar el registro.';
            if (errData.error) {
                errMsg = errData.error;
            } else if (typeof errData === 'object') {
                errMsg = Object.entries(errData).map(([k, v]) => `${k}: ${v}`).join('<br>');
            }
            showToast(errMsg, 'error');
        }
    } catch (err) {
        showToast('Error de conexión con el servidor', 'error');
        console.error(err);
    }
}

// --- DELETE RESOURCE ---
async function deleteResource(resourceType, id) {
    if (!confirm('¿Estás seguro de que deseas eliminar este registro? Esto podría desvincular capturas asociadas.')) {
        return;
    }
    
    try {
        const response = await fetch(`${API[resourceType]}/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showToast('Registro eliminado con éxito.', 'success');
            refreshViewData(state.currentView);
        } else {
            const errData = await response.json();
            showToast(errData.error || 'No se pudo eliminar el registro.', 'error');
        }
    } catch (err) {
        showToast('Error de conexión', 'error');
    }
}

// --- RELEASE CAPTURE RELATION ---
window.liberarPokemon = async function(pokemonId, entrenadorId) {
    if (!confirm('¿Estás seguro de que deseas liberar a este Pokémon? Se eliminará de la base de datos de capturas.')) {
        return;
    }
    
    try {
        const response = await fetch(`/api/capturas/pokemon/${pokemonId}/entrenador/${entrenadorId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showToast('El Pokémon ha sido liberado en la naturaleza.', 'success');
            refreshViewData(state.currentView);
        } else {
            const errData = await response.json();
            showToast(errData.error || 'No se pudo liberar el Pokémon.', 'error');
        }
    } catch (err) {
        showToast('Error de conexión', 'error');
    }
};

// Expose openEditModal globally so onclick works
window.openEditModal = openEditModal;
window.deleteResource = deleteResource;

// --- TOAST NOTIFICATIONS ---
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    
    const icon = type === 'success' ? 'fa-circle-check' : 'fa-triangle-exclamation';
    
    toast.innerHTML = `
        <i class="fa-solid ${icon} toast-icon"></i>
        <div class="toast-content">${message}</div>
    `;
    
    elements.toastContainer.appendChild(toast);
    
    // Auto remove
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(50px)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

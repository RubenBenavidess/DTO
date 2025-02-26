document.addEventListener("DOMContentLoaded", function() {
    obtenerPropietarios();
    obtenerAutomoviles();
    obtenerSeguros();
});

let metodo = "POST";
let rutaP = "http://localhost:9090/api/propietarios";
let rutaA = "http://localhost:9090/api/automoviles";
let rutaS = "http://localhost:9090/api/seguros";

async function obtenerPropietarios() {
    let response = await fetch("http://localhost:9090/api/propietarios");
    let data = await response.json();
    let tbody = document.getElementById("tabla-propietarios");
    tbody.innerHTML = "";
    for(propietario of data){
        let vehiculos = "";
        for(automovilId in propietario.automovilesIds){
            vehiculos += await obtenerAutomovil(propietario.automovilesIds[automovilId], "modelo") + "<br>";
        }
        tbody.innerHTML += `<tr>
            <td>${propietario.id}</td>
            <td>${propietario.nombreCompleto}</td>
            <td>${propietario.edad}</td>
            <td>${vehiculos}</td>
            <td>
                <button class='btn btn-warning btn-sm' data-bs-toggle="modal" data-bs-target="#modalPropietario" onclick='modificarPropietario(${propietario.id})'>Modificar</button>
                <button class='btn btn-danger btn-sm' data-bs-toggle="modal" data-bs-target="#modalPropietario" onclick='eliminarPropietario(${propietario.id})'>Eliminar</button>
            </td>
        </tr>`;
    }
}

let obtenerPropietario = async (id) => {
    let response = await fetch(`http://localhost:9090/api/propietarios/${id}`);
    let data = await response.json();
    return data.nombreCompleto;
}

async function cargarPropietariosSelect() {
    let response = await fetch("http://localhost:9090/api/propietarios");
    let data = await response.json();
    let selectPropietario = document.querySelectorAll(".propietarioId");
    selectPropietario.forEach((select) => {
        select.innerHTML = "";
        data.forEach((propietario) => {
            select.innerHTML += `<option value="${propietario.id}">${propietario.nombreCompleto}</option>`;
        });
    });
}

async function cargarAutomovilesSelect() {
    let selectPropietario = document.querySelectorAll(".propietarioId");
    selectPropietario.disabled = false;
    let id = selectPropietario[1].value;

    let response = await fetch(`http://localhost:9090/api/propietarios/${id}`);
    let data = await response.json();
    let automoviles = [];
    for(automovilId in data.automovilesIds){
        automoviles.push(await obtenerAutomovil(data.automovilesIds[automovilId], "modelo"));
    }
    let selectVehiculo = document.getElementById("automovilId");
    selectVehiculo.innerHTML = "";
    for(i in automoviles){
        selectVehiculo.innerHTML += `<option value="${data.automovilesIds[i]}">${automoviles[i]}</option>`;
    }
}

async function obtenerAutomoviles() {
    let response = await fetch("http://localhost:9090/api/automoviles");
    let data = await response.json();
    let tbody = document.getElementById("tabla-automoviles");
    tbody.innerHTML = "";

    for (let automovil of data) {
        let nombrePropietario = await obtenerPropietario(automovil.propietarioId);
        
        tbody.innerHTML += `<tr>
            <td>${automovil.id}</td>
            <td>${automovil.modelo}</td>
            <td>${automovil.valor}</td>
            <td>${automovil.accidentes}</td>
            <td>${nombrePropietario}</td>
            <td>
                <button class='btn btn-warning btn-sm' data-bs-toggle="modal" data-bs-target="#modalAutomovil" onclick='modificarAutomovil(${automovil.id})'>Modificar</button>
                <button class='btn btn-danger btn-sm' data-bs-toggle="modal" data-bs-target="#modalAutomovil" onclick='eliminarAutomovil(${automovil.id})'>Eliminar</button>
            </td>
        </tr>`;
    }
}

let obtenerAutomovil = async (id, req = "") => {
    let response = await fetch(`http://localhost:9090/api/automoviles/${id}`);
    let data = await response.json();
    if(req == "modelo")
        return data.modelo;
    if(req == "idPropietario")
        return data.propietarioId;
    return data;
}

async function obtenerSeguros() {
    let response = await fetch("http://localhost:9090/api/seguros");
    let data = await response.json();
    let tbody = document.getElementById("tabla-seguros");
    tbody.innerHTML = "";

    for (let seguro of data) {
        
        let propietarioData = await obtenerAutomovil(seguro.automovilId);
        let propietarioId = propietarioData.propietarioId;
        let propietario = await obtenerPropietario(propietarioId);
        let automovil = await obtenerAutomovil(seguro.automovilId, "modelo");
        tbody.innerHTML += `<tr>
            <td>${seguro.id}</td>
            <td>${seguro.costoTotal}</td>
            <td>${propietario}</td>
            <td>${automovil}</td>
            <td>
                <button class='btn btn-warning btn-sm' data-bs-toggle="modal" data-bs-target="#modalSeguro" onclick='modificarSeguro(${seguro.id})'>Modificar</button>
                <button class='btn btn-danger btn-sm' data-bs-toggle="modal" data-bs-target="#modalSeguro" onclick='eliminarSeguro(${seguro.id})'>Eliminar</button>
            </td>
        </tr>`;
    }
}

function resetearP(){
    document.getElementById("formPropietario").reset();

    let label = document.getElementById('modalPropietarioLabel');
    label.innerHTML = "Añadir Propietario";

    let button = document.getElementById('btn-propietario');
    button.textContent = "Agregar";

    const nombre = document.getElementById("nombreCompleto");
    const edad = document.getElementById("edad");

    nombre.disabled = false;
    edad.disabled = false;

    metodo = "POST";
    rutaP = "http://localhost:9090/api/propietarios";
}

function resetearA(){
    document.getElementById("formAutomovil").reset();

    let label = document.getElementById('modalAutomovilLabel');
    label.innerHTML = "Añadir Automóvil";

    let button = document.getElementById('btn-automovil');
    button.textContent = "Agregar";

    const modelo = document.getElementById("modelo");
    const valor = document.getElementById("valor");
    const accidentes = document.getElementById("accidentes");
    const idPropietario = document.querySelector(".propietarioId");

    modelo.disabled = false;
    valor.disabled = false;
    accidentes.disabled = false;
    idPropietario.disabled = false;

    metodo = "POST";
    rutaA = "http://localhost:9090/api/automoviles";
}

function resetearS(){
    document.getElementById("formSeguro").reset();

    let label = document.getElementById('modalSeguroLabel');
    label.innerHTML = "Añadir Seguro";

    let button = document.getElementById('btn-seguro');
    button.textContent = "Agregar";
    
    const costoTotal = document.getElementById("costoTotal");
    const idPropietario = document.querySelectorAll(".propietarioId")[1];
    const idAutomovil = document.getElementById("automovilId");

    costoTotal.disabled = false;
    idPropietario.disabled = false;
    idAutomovil.disabled = false;

    metodo = "POST";
    rutaS = "http://localhost:9090/api/seguros";
}

async function modificarPropietario(id){
    rutaP = "http://localhost:9090/api/propietarios";

    const label = document.getElementById('modalPropietarioLabel');
    label.innerHTML = "Modificar Propietario";

    const button = document.getElementById('btn-propietario');
    button.textContent = "Modificar";
    
    let response = await fetch(`http://localhost:9090/api/propietarios/${id}`);
    let data = await response.json();

    const nombre = document.getElementById("nombreCompleto");
    const edad = document.getElementById("edad");

    nombre.value = data.nombreCompleto;
    edad.value = data.edad;
    
    nombre.disabled = false;
    edad.disabled = false;

    metodo = "PUT";
    rutaP = rutaP += `/${id}`;
}

async function eliminarPropietario(id){
    rutaP = "http://localhost:9090/api/propietarios";

    const label = document.getElementById('modalPropietarioLabel');
    label.innerHTML = "Eliminar Propietario";

    const button = document.getElementById('btn-propietario');
    button.textContent = "Confirmar";

    let response = await fetch(`http://localhost:9090/api/propietarios/${id}`);
    let data = await response.json();

    const nombre = document.getElementById("nombreCompleto");
    const edad = document.getElementById("edad");

    nombre.value = data.nombreCompleto;
    edad.value = data.edad;

    nombre.disabled = true;
    edad.disabled = true;

    metodo = "DELETE";
    rutaP = rutaP += `/${id}`;

}


document.getElementById("formPropietario").addEventListener("submit", async function(event) {
    event.preventDefault();

    const nombre = document.getElementById("nombreCompleto").value;
    const edad = document.getElementById("edad").value;
    const cuerpo = metodo == "POST" || metodo == "PUT" ? 
    {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ nombreCompleto: nombre, edad: edad })
    } :
    {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        }
    };
    await fetch(rutaP, cuerpo);
    obtenerPropietarios(); 
    obtenerAutomoviles(); 
    obtenerSeguros();
    document.getElementById("formPropietario").reset();
    bootstrap.Modal.getInstance(document.getElementById('modalPropietario')).hide();
    metodo = "POST";
    rutaP = "http://localhost:9090/api/propietarios";
});

async function modificarAutomovil(id){
    
    rutaA = "http://localhost:9090/api/automoviles";

    const label = document.getElementById('modalAutomovilLabel');
    label.innerHTML = "Modificar Automóvil";

    const button = document.getElementById('btn-automovil');
    button.textContent = "Modificar";
    
    let response = await fetch(`http://localhost:9090/api/automoviles/${id}`);
    let data = await response.json();

    const modelo = document.getElementById("modelo");
    const valor = document.getElementById("valor");
    const accidentes = document.getElementById("accidentes");
    const idPropietario = document.querySelector(".propietarioId");

    modelo.value = data.modelo;
    valor.value = data.valor;
    accidentes.value = data.accidentes;

    idPropietario.innerHTML = `<option value="${await data.propietarioId}">${await obtenerPropietario(await data.propietarioId)}</option>`;

    modelo.disabled = false;
    valor.disabled = false;
    accidentes.disabled = false;
    idPropietario.disabled = false;

    metodo = "PUT";
    rutaA = rutaA += `/${id}`;
}

async function eliminarAutomovil(id){
    rutaA = "http://localhost:9090/api/automoviles";

    const label = document.getElementById('modalAutomovilLabel');
    label.innerHTML = "Eliminar Automóvil";

    const button = document.getElementById('btn-automovil');
    button.textContent = "Confirmar";

    let response = await fetch(`http://localhost:9090/api/automoviles/${id}`);
    let data = await response.json();

    const modelo = document.getElementById("modelo");
    const valor = document.getElementById("valor");
    const accidentes = document.getElementById("accidentes");
    const idPropietario = document.querySelector(".propietarioId");

    modelo.value = data.modelo;
    valor.value = data.valor;
    accidentes.value = data.accidentes;

    idPropietario.innerHTML = `<option value="${await data.propietarioId}">${await obtenerPropietario(await data.propietarioId)}</option>`;

    modelo.disabled = true;
    valor.disabled = true;
    accidentes.disabled = true;
    idPropietario.disabled = true;

    metodo = "DELETE";
    rutaA = rutaA += `/${id}`;

}

document.getElementById("formAutomovil").addEventListener("submit", async function(event) {
    event.preventDefault();
    const modelo = document.getElementById("modelo").value;
    const valor = document.getElementById("valor").value;
    const accidentes = document.getElementById("accidentes").value;
    const idPropietario = document.querySelector(".propietarioId").value;
    const cuerpo = metodo == "POST" || metodo == "PUT" ? 
    {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ accidentes: accidentes, modelo: modelo, valor: valor, propietarioId: idPropietario })
    } :
    {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        }
    };
    await fetch(rutaA, cuerpo);
    obtenerPropietarios();
    obtenerAutomoviles(); 
    obtenerSeguros();
    document.getElementById("formAutomovil").reset();
    bootstrap.Modal.getInstance(document.getElementById('modalAutomovil')).hide();
    
    metodo = "POST";
    rutaA = "http://localhost:9090/api/automoviles";
});


async function modificarSeguro(id){
    rutaS = "http://localhost:9090/api/seguros";

    const label = document.getElementById('modalSeguroLabel');
    label.innerHTML = "Modificar Seguro";

    const button = document.getElementById('btn-seguro');
    button.textContent = "Modificar";
    
    let response = await fetch(`http://localhost:9090/api/seguros/${id}`);
    let data = await response.json();

    let propietarioData = await obtenerAutomovil(data.automovilId);
    let propietarioId = propietarioData.propietarioId;
    let propietario = await obtenerPropietario(propietarioId);

    const costoTotal = document.getElementById("costoTotal");
    const propietarioSelect = document.querySelectorAll(".propietarioId")[1];

    costoTotal.value = data.costoTotal;
    propietarioSelect.innerHTML = `<option value="${propietarioId}">${propietario}</option>`;

    let selectPropietario = document.querySelectorAll(".propietarioId");
    selectPropietario.disabled = false;
    let id2 = selectPropietario[1].value;

    let response2 = await fetch(`http://localhost:9090/api/propietarios/${id2}`);
    let data2 = await response2.json();
    let automoviles = [];
    for(automovilId in data2.automovilesIds){
        automoviles.push(await obtenerAutomovil(data2.automovilesIds[automovilId], "modelo"));
    }
    let selectVehiculo = document.getElementById("automovilId");
    selectVehiculo.innerHTML = "";
    for(i in automoviles){
        selectVehiculo.innerHTML += `<option value="${data2.automovilesIds[i]}">${automoviles[i]}</option>`;
    }

    selectVehiculo.value = await data.automovilId;

    costoTotal.disabled = false;
    propietarioSelect.disabled = false;

    metodo = "PUT";
    rutaS = rutaS += `/${id}`;
}


async function eliminarSeguro(id){
    rutaS = "http://localhost:9090/api/seguros";

    const label = document.getElementById('modalSeguroLabel');
    label.innerHTML = "Eliminar Seguro";

    const button = document.getElementById('btn-seguro');
    button.textContent = "Confirmar";
    
    let response = await fetch(`http://localhost:9090/api/seguros/${id}`);
    let data = await response.json();

    let propietarioData = await obtenerAutomovil(data.automovilId);
    let propietarioId = propietarioData.propietarioId;
    let propietario = await obtenerPropietario(propietarioId);

    const costoTotal = document.getElementById("costoTotal");
    const propietarioSelect = document.querySelectorAll(".propietarioId")[1];

    costoTotal.value = data.costoTotal;
    propietarioSelect.innerHTML = `<option value="${propietarioId}">${propietario}</option>`;

    let selectVehiculo = document.getElementById("automovilId");
    modelo = obtenerAutomovil(data.automovilId, "modelo");
    selectVehiculo.innerHTML = `<option value="${await data.automovilId}">${await modelo}</option>`;

    costoTotal.disabled = true;
    propietarioSelect.disabled = true;
    selectVehiculo.disabled = true;

    metodo = "DELETE";
    rutaS = rutaS += `/${id}`;
}


document.getElementById("formSeguro").addEventListener("submit", async function(event) {
    event.preventDefault();

    const costoTotal = document.getElementById("costoTotal").value;
    const idAutomovil = document.getElementById("automovilId").value;

    const cuerpo = metodo == "POST" || metodo == "PUT" ? 
    {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ costoTotal: costoTotal, automovilId: idAutomovil})
    } :
    {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        }
    };
    console.log(cuerpo);
    await fetch(rutaS, cuerpo);
    obtenerPropietarios();
    obtenerAutomoviles();
    obtenerSeguros(); 
    document.getElementById("formSeguro").reset();
    bootstrap.Modal.getInstance(document.getElementById('modalSeguro')).hide();
    
    metodo = "POST";
    rutaS = "http://localhost:9090/api/seguros";
});

document.getElementById("btn-add-p").addEventListener("click", function() {
    resetearP();
});

document.getElementById("btn-add-a").addEventListener("click", function() {
    resetearA();
    cargarPropietariosSelect();
});

document.getElementById("btn-add-s").addEventListener("click", async function() {
    resetearS();
    await cargarPropietariosSelect();
    await cargarAutomovilesSelect();
});

document.querySelectorAll(".propietarioId")[1].addEventListener("change", async function() {
    await cargarAutomovilesSelect();
});
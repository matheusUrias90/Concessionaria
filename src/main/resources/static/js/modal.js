function bloquearMenu() {

    const menu = document.querySelector(".menu-lateral");

    if (menu) {
        menu.classList.add("modal-aberta");
    }

}

function liberarMenu() {

    const menu = document.querySelector(".menu-lateral");

    if (menu) {
        menu.classList.remove("modal-aberta");
    }

}

function abrirModal(id) {

    const modal = document.getElementById(id);

    if (!modal) {
        console.error("Modal não encontrada:", id);
        return;
    }

    modal.style.display = "block";

    bloquearMenu();

}

function fecharModal(id) {

    const modal = document.getElementById(id);

    if (!modal) {
        console.error("Modal não encontrada:", id);
        return;
    }

    modal.style.display = "none";

    liberarMenu();

}

document.addEventListener("click", function (event) {

    if (!event.target.classList.contains("modal")) {
        return;
    }

    event.target.style.display = "none";

    liberarMenu();

});

document.addEventListener("keydown", function (event) {

    if (event.key !== "Escape") {
        return;
    }

    document.querySelectorAll(".modal").forEach(function (modal) {
        modal.style.display = "none";
    });

    liberarMenu();

});
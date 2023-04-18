document.addEventListener('DOMContentLoaded', function () {
    const customHrefs = document.querySelectorAll('[data-href]');
    const deleteModals = document.querySelectorAll('[data-modal-delete]');
    deleteModals.forEach(function (element) {
        element.addEventListener('click', function (event) {
            const modalID = event.target.getAttribute('data-bs-target');
            const deleteHref = event.target.getAttribute('data-modal-delete');
            document.querySelector(modalID).querySelector('[data-href]').setAttribute('data-href', deleteHref);
        })
    })
    customHrefs.forEach(function (element) {
        element.addEventListener('click', function (event) {
            if (event.target.tag !== 'a') window.location.href = this.dataset.href;
        })
    })
})

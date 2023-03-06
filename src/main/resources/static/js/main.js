document.addEventListener('DOMContentLoaded', function () {
    const customHrefs = document.querySelectorAll('[data-href]');
    customHrefs.forEach(function (element) {
        element.addEventListener('click', function (event) {
            if (event.target.tag !== 'a') window.location.href = this.dataset.href;
        })
    })
})

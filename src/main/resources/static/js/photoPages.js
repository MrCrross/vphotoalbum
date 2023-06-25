const updatePage = (photos) => {
    const cardTemplate = $(`<div class="card shadow-sm col-sm-12 col-md-4 col-lg-2_5 me-md-2 mb-sm-2 p-2">
                    <img src="" class="card-img-top openFullImg" alt="...">
                    <div class="card-body">
                        <h5 class="card-title"></h5>
                        <p class="card-text"></p>
                        <div class="d-flex justify-content-between align-items-center">
                            <div class="btn-group">
                                <a href="" class=" btn btn-sm btn-outline-secondary">Открыть</a>
                            </div>
                            <small class="text-body-secondary"></small>
                        </div>
                    </div>
                </div>`);
    const container = $('#photoCards');
    container.find('.card').remove();
    photos.forEach((photo) => {
        const cloneCard = cardTemplate.clone();
        cloneCard.find('img').attr('src', photo.path);
        cloneCard.find('h5').text(photo.name);
        cloneCard.find('p').text(photo.description);
        cloneCard.find('a').attr('href', '/photo/' + photo.id);
        cloneCard.find('small').text(photo.dateAdd);
        container.append(cloneCard);
    })
}

const updatePagination = (currentPage, maxPage) => {
    const paginationPrev = $(`<li class="page-item paginate_item">
                        <a class="page-link" type="button" aria-label="Предыдущая">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>`);
    const paginationItem = $(`<li class="page-item paginate_item"><a class="page-link" data-page="" type="button"></a></li>`);
    const paginationNext = $(`<li class="page-item paginate_item">
                        <a class="page-link" type="button" aria-label="Следующая">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>`);

    const pagination = $('#pagination');
    const clonePrev = paginationPrev.clone();
    const cloneNext = paginationNext.clone();
    pagination.find('li').remove();
    if (currentPage === 1) {
        clonePrev.addClass('disabled');
        clonePrev.removeClass('paginate_item');
    } else {
        clonePrev.find('a').attr('data-page', currentPage - 1);
    }
    pagination.append(clonePrev);
    for(let i= 1; i <= maxPage; i++) {
        const cloneItem = paginationItem.clone();
        cloneItem.find('a').text(i);
        if (i === currentPage) {
            cloneItem.addClass('active');
            cloneItem.removeClass('paginate_item');
        }
        cloneItem.find('a').attr('data-page', i);
        pagination.append(cloneItem);
    }
    if (currentPage === maxPage) {
        cloneNext.addClass('disabled');
        cloneNext.removeClass('paginate_item');
    } else {
        cloneNext.find('a').attr('data-page', currentPage + 1);
    }
    pagination.append(cloneNext);
}

const buildCategoryTree = (category, objectTree) => {
    objectTree.text = category.name;
    objectTree.id = category.id;
    objectTree.class = "category_item";
    if (category.children !== null && category.children.length !== 0) {
        objectTree.nodes = [];
        category.children.forEach((el) => {
            objectTree.nodes.push(buildCategoryTree(el, {}));
        })
    }
    return objectTree;
}

$('document').ready(() => {
    $.ajax('/api/photo/category/tree', {
        method: 'GET',
        dataType: 'json',
        success: (data) => {
            const tree = [];
            data.forEach((el) => {
                tree.push(buildCategoryTree(el, {}))
            })
            $('#category_tree').bstreeview({
                data: tree,
                indent: 0.8,
                parentsMarginLeft: '0.8rem',
                expandIcon: "fa fa-angle-down fa-fw", collapseIcon: "fa fa-angle-right fa-fw"
            });
            $('.category_item').each((index, e) => {
                const text = $(e).html();
                const id = $(e).attr('id');
                $(e).html(`<div class="form-check">
                  <input class="form-check-input category_check" type="checkbox" value="${id}" id="categoryCheck${id}">
                  <label class="form-check-label" for="categoryCheck${id}">
                    ${text}
                  </label>
                </div>`)
                $(e).attr('data-id', id);
                $(e).removeAttr('id');
            })
            $('#category_tree').append('<button id="submitCategories" type="button" class="btn btn-outline-success">Применить</button>')
        }
    })
    $.ajax('/api/photo/get', {
        method: 'GET',
        dataType: 'json',
        success: (data) => {
            updatePagination(data.currentPage, data.maxPage);
            updatePage(data.data)
        }
    })
})
const fullImgModal = new bootstrap.Modal($('#fullImgModal'), {});
$('#photoCards').on('click', '.openFullImg', (e) => {
    const src = $(e.target).attr('src');
    const alt = $(e.target).attr('alt');
    const modal = $('#fullImgModal');
    modal.find('img').attr('src', src);
    modal.find('img').attr('alt', alt);
    fullImgModal.show();
})

$('#pagination').on('click', '.paginate_item', (e) => {
    const target = $(e.target);
    let page = 1;
    if (target.prop("tagName") === 'A') {
        page = target.attr('data-page');
    } else if (target.prop("tagName") === 'SPAN') {
        page = target.parent().attr('data-page');
    } else if (target.prop("tagName") === 'LI') {
        page = target.find('a').attr('data-page');
    }
    $.ajax('/api/photo/get', {
        method: 'GET',
        dataType: 'json',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: {
            page
        },
        success: (data) => {
            updatePagination(data.currentPage, data.maxPage);
            updatePage(data.data)
        }
    })
})

$('#category_tree').on('click', '#submitCategories', () => {
    const category = [];
    $('.category_check:checked').each((i, check) => {
        category.push($(check).val());
    })
    $.ajax('/api/photo/get', {
        method: 'GET',
        dataType: 'json',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: {
            page: 1,
            category,
        },
        success: (data) => {
            updatePagination(data.currentPage, data.maxPage);
            updatePage(data.data)
        }
    })
})
$('#photoSearch').click(() => {
    const textSearch = $('[name="search"]').val()
    const category = [];
    $('.category_check:checked').each((i, check) => {
        category.push($(check).val());
    })
    $.ajax('/api/photo/get', {
        method: 'GET',
        dataType: 'json',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: {
            page: 1,
            category,
            search: textSearch
        },
        success: (data) => {
            updatePagination(data.currentPage, data.maxPage);
            updatePage(data.data)
        }
    })
})
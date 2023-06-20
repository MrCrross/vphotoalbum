const createAlbumModalBootstrap = $('#createAlbumModal').length != 0 ? new bootstrap.Modal($('#createAlbumModal'), {}) : null;
const createPhotoModalBootstrap = $('#createPhotoModal').length != 0 ? new bootstrap.Modal($('#createPhotoModal'), {}) : null;
const editPhotoModalBootstrap = $('#editPhotoModal').length != 0 ? new bootstrap.Modal($('#editPhotoModal'), {})  : null;

const updateBreadCrumb = (parents) => {
    const breadcrumb = $('#breadcrumb');
    breadcrumb.find('li').remove();
    let newBreadcrumb = [];
    breadcrumb.append($('<li class="breadcrumb-item"><a class="photo_link" data-id="0" type="button">Фотоальбомы</a></li>'));
    if (parents.length > 0) {
        parents.forEach((el, i) => {
            if (i === 0) {
                newBreadcrumb.push(`<li class="breadcrumb-item active">${el.name}</li>`);
            } else {
                newBreadcrumb.push(`<li class="breadcrumb-item"><a class="photo_link" data-id="${el.id}" type="button">${el.name}</a></li>`);
            }

        })
        newBreadcrumb = newBreadcrumb.reverse();
        newBreadcrumb.forEach((el) => {
            breadcrumb.append($(el));
        })
    }
}
const updateTree = (tree) => {
    const photosTree = $('#photos_tree');
    photosTree.find('li').remove();
    if (tree.length > 0) {
        tree.forEach((el) => {
            if (el.type === 'folder') {
                photosTree.append(`<li class="list-group-item">
                    <a class="photo_link d-flex justify-content-between align-items-center text-decoration-none" data-id="${el.id}" type="button">
                        <div>
                            <img src="${el.path}" alt="" width="18" height="18">
                            <span>${el.name}</span>
                        </div>
                        <span>${el.date_add}</span>
                    </a>
                </li>`);
            } else {
                photosTree.append(`<li class="list-group-item">
                    <a class="d-flex justify-content-between align-items-center text-decoration-none" href="/photo/${el.id}">
                        <div>
                            <img src="${el.path}" alt="" width="18" height="18">
                            <span>${el.name}</span>
                        </div>
                        <span>${el.date_add}</span>
                    </a>
                </li>`);
            }

        })
    } else {
        photosTree.append('<li class="list-group-item">Пока альбом пуст</li>')
    }
}

const toastsBodyClear = ()=>{
    $('#toastDanger').find('.toast-body').html('');
    $('#toastSuccess').find('.toast-body').html('');
}

$('#photos_card').click('.photo_link', (e) => {
    const target = $(e.target);
    let id = 0;
    if (target.hasClass('photo_link')) {
        id = parseInt(target.data('id'));
    } else {
        id = target.parents('.photo_link').data('id')
    }
    let url = '/api/photo/tree';
    if (id !== 0 && !isNaN(id)) {
        url = '/api/photo/tree?id=' + id;
    }
    $.ajax(url, {
        method: 'GET',
        dataType: 'json',
        success: (data) => {
            updateBreadCrumb(data[0]);
            updateTree(data[1]);
        }
    })
})
$('#openCreateAlbumModal').click(() => {
    $.ajax('/api/photo/album/get', {
        method: 'GET',
        dataType: 'json',
        success: (albums) => {
            const createAlbumForm = $('#createAlbumForm');
            createAlbumForm.find('#albumName').val('');
            createAlbumForm.find('#albumDescription').val('');
            createAlbumForm.find('#albumParent option').remove();
            createAlbumForm.find('#albumParent').append("<option value='0'>Нет родителя</option>");
            albums.forEach((album) => {
                createAlbumForm.find('#albumParent').append("<option value='" + album.id + "'>" + album.name + "</option>");
            })
        }
    })
    createAlbumModalBootstrap.show();
})

$("#createAlbumSubmit").click(() => {
    const createAlbumForm = $('#createAlbumForm');
    const name = createAlbumForm.find('[name="name"]').val();
    const description = createAlbumForm.find('[name="description"]').val();
    const parentID = createAlbumForm.find('[name="parent_id"]').val();
    const toastDanger = $('#toastDanger');
    if (!$.trim(name).length) {
        toastsBodyClear();
        toastDanger.find('.toast-body').html('Заполните поле "Название альбома"');
        const toast = new bootstrap.Toast(toastDanger, {});
        toast.show();
        return;
    }

    $.ajax('/api/photo/album', {
        method: 'POST',
        dataType: 'json',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            name,
            description,
            parentID
        }),
        success: (req) => {
            toastsBodyClear();
            const toastSuccess = $('#toastSuccess');
            toastSuccess.find('.toast-body').html('Альбом успешно добавлен');
            const toast = new bootstrap.Toast(toastSuccess, {});
            toast.show();
            createAlbumModalBootstrap.hide();
        },
        error: (req) => {
            toastsBodyClear();
            toastDanger.find('.toast-body').html(req.status);
            const toast = new bootstrap.Toast(toastDanger, {});
            toast.show();
        }
    })
})

$('#openCreatePhotoModal').click(() => {
    $.ajax('/api/photo/album/get', {
        method: 'GET',
        dataType: 'json',
        success: (albums) => {
            const createPhotoForm = $('#createPhotoForm');
            createPhotoForm.find('#photoFile').val('');
            createPhotoForm.find('#photoName').val('');
            createPhotoForm.find('#photoDescription').val('');
            createPhotoForm.find('#photoAlbum option').remove();
            createPhotoForm.find('#photoAlbum').append("<option value='0'>Нет альбома</option>");
            albums.forEach((album) => {
                createPhotoForm.find('#photoAlbum').append("<option value='" + album.id + "'>" + album.name + "</option>");
            })
        }
    })
    createPhotoModalBootstrap.show();
})

$("#createPhotoSubmit").click(() => {
    const createPhotoForm = $('#createPhotoForm');
    const formData = new FormData();
    const file = createPhotoForm.find('[name="file"]');
    const name = createPhotoForm.find('[name="name"]').val();
    const description = createPhotoForm.find('[name="description"]').val();
    const albumID = createPhotoForm.find('[name="albumID"]').val();
    const toastDanger = $('#toastDanger');
    if (!$.trim(name).length) {
        toastsBodyClear();
        toastDanger.find('.toast-body').html('Заполните поле "Название"');
        const toast = new bootstrap.Toast(toastDanger, {});
        toast.show();
        return;
    }
    if (file[0].files.length === 0) {
        toastsBodyClear();
        toastDanger.find('.toast-body').html('Выберите фотографию');
        const toast = new bootstrap.Toast(toastDanger, {});
        toast.show();
        return;
    }
    formData.append('file', file[0].files[0]);
    formData.append('name', name);
    formData.append('description', description);
    formData.append('albumID', albumID);
    $.ajax('/api/photo', {
        method: 'POST',
        dataType: 'json',
        contentType: false,
        processData: false,
        data: formData,
        success: (req) => {
            toastsBodyClear();
            const toastSuccess = $('#toastSuccess');
            toastSuccess.find('.toast-body').html('Фотография успешно добавлен');
            const toast = new bootstrap.Toast(toastSuccess, {});
            toast.show();
            createPhotoModalBootstrap.hide();
        },
        error: (req) => {
            toastsBodyClear();
            toastDanger.find('.toast-body').html(req.status);
            const toast = new bootstrap.Toast(toastDanger, {});
            toast.show();
        }
    })
})

$('#openEditPhotoModal').click(() => {
    $.ajax('/api/photo/album/get', {
        method: 'GET',
        dataType: 'json',
        success: (albums) => {
            const editPhotoForm = $('#editPhotoForm');
            const currentAlbumID = editPhotoForm.find('#photoAlbumID').val();
            editPhotoForm.find('#photoAlbum option').remove();
            editPhotoForm.find('#photoAlbum').append("<option value='0'>Нет альбома</option>");
            albums.forEach((album) => {
                const selected = album.id === parseInt(currentAlbumID) ? 'selected' : '';
                editPhotoForm.find('#photoAlbum').append("<option value='" + album.id + "'" + selected + ">" + album.name + "</option>");
            })
        }
    })
    editPhotoModalBootstrap.show();
})

$("#editPhotoSubmit").click(() => {
    const editPhotoForm = $('#editPhotoForm');
    const formData = new FormData();
    const id = editPhotoForm.find('[name="id"]').val();
    const ownerID = editPhotoForm.find('[name="ownerID"]').val();
    const file = editPhotoForm.find('[name="file"]');
    const name = editPhotoForm.find('[name="name"]').val();
    const description = editPhotoForm.find('[name="description"]').val();
    const albumID = editPhotoForm.find('[name="albumID"]').val();
    const toastDanger = $('#toastDanger');
    if (!$.trim(name).length) {
        toastsBodyClear();
        toastDanger.find('.toast-body').html('Заполните поле "Название"');
        const toast = new bootstrap.Toast(toastDanger, {});
        toast.show();
        return;
    }
    if (file[0].files.length !== 0) {
        formData.append('file', file[0].files[0]);
    }
    formData.append('id', id);
    formData.append('ownerID', ownerID);
    formData.append('name', name);
    formData.append('description', description);
    formData.append('albumID', albumID);
    $.ajax('/api/photo/' + id, {
        method: 'POST',
        dataType: 'json',
        contentType: false,
        processData: false,
        data: formData,
        success: (req) => {
            toastsBodyClear();
            const toastSuccess = $('#toastSuccess');
            toastSuccess.find('.toast-body').html('Фотография успешно изменена');
            const toast = new bootstrap.Toast(toastSuccess, {});
            toast.show();
            $('#photoAlbumID').val(req.albumID);
            $('#cardAlbum').html("Альбом: " + req.albumName);
            $('#photoImg').attr("src", req.path);
            $('#cardImg').attr("src", req.path);
            $('#photoName').val(req.name);
            $('#cardName').html("Название: " + req.name);
            $('#photoDescription').val(req.description);
            $('#cardDescription').html("Описание: " + req.description);
            $('#cardDateEdit').html("Изменена: " + req.dateEdit);
            editPhotoModalBootstrap.hide();
        },
        error: (req) => {
            toastsBodyClear();
            toastDanger.find('.toast-body').html(req.status);
            const toast = new bootstrap.Toast(toastDanger, {});
            toast.show();
        }
    })
})
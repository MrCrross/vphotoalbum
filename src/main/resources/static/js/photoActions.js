const createAlbumModalBootstrap = $('#createAlbumModal').length != 0 ? new bootstrap.Modal($('#createAlbumModal'), {}) : null;
const createPhotoModalBootstrap = $('#createPhotoModal').length != 0 ? new bootstrap.Modal($('#createPhotoModal'), {}) : null;
const editPhotoModalBootstrap = $('#editPhotoModal').length != 0 ? new bootstrap.Modal($('#editPhotoModal'), {})  : null;
const editPhotoViewersModalBootstrap = $('#editPhotoViewersModal').length != 0 ? new bootstrap.Modal($('#editPhotoViewersModal'), {})  : null;
const editAlbumModalBootstrap = $('#editAlbumModal').length != 0 ? new bootstrap.Modal($('#editAlbumModal'), {})  : null;
const editAlbumViewersModalBootstrap = $('#editAlbumViewersModal').length != 0 ? new bootstrap.Modal($('#editAlbumViewersModal'), {})  : null;

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
const updateTree = (tree, owners) => {
    const photosTree = $('#photos_tree');
    photosTree.find('li').remove();
    if (tree.length > 0) {
        let editAlbum = '';
        let owner = '';
        let hrefPhoto = '';
        tree.forEach((el) => {
            console.log(el.owner_id === owners.current)
            console.log(el.owner_id)
            console.log(owners.current)
            if (el.owner_id === owners.current) {
                editAlbum = `<span><a href="/photo/album/${el.id}"><i class="fa-solid fa-pen"></i></a></span>`;
                hrefPhoto = `href="/photo/${el.id}"`;
            } else {
                owner = el.owner_name;
            }
            if (el.type === 'folder') {
                photosTree.append(`<li class="list-group-item">
                    <div class="photo_link d-flex justify-content-between align-items-center text-decoration-none cursor-pointer" data-id="${el.id}">
                        <div>
                            <img src="${el.path}" alt="" width="18" height="18">
                            <span>${el.name}</span>
                        </div>
                        <div>
                            ${editAlbum}
                            ${owner}
                            <span>${el.date_add}</span>
                        </div>
                    </div>
                </li>`);
            } else {
                photosTree.append(`<li class="list-group-item">
                    <a class="d-flex justify-content-between align-items-center text-decoration-none" ${hrefPhoto}>
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

const updateViewers = (form, id, viewers) => {
    const container = $('#viewers_container');
    container.find('.viewer_item').remove();
    const viewerItem = form.find('#viewer_item_temp');
    form.find('[name="id"]').attr('value', id);
    if (viewers.length !== 0) {
        viewers.forEach((viewer, index) => {
            const clone = viewerItem.clone();
            clone.removeAttr('id')
            clone.removeClass('visually-hidden');
            clone.find('select').attr('name', 'viewers');
            clone.find('select option').each((key, option) => {
                if (parseInt($(option).attr('value')) === viewer.viewer) {
                    $(option).attr('selected', 'selected');
                }
            })

            if (index > 0) {
                clone.find('button').remove();
                clone.append('<button class="btn btn-danger removeViewer" type="button">-</button>')
            }
            container.append(clone);
        })
    } else {
        const clone = viewerItem.clone();
        clone.removeAttr('id')
        clone.removeClass('visually-hidden');
        clone.find('select').attr('name', 'viewers');
        container.append(clone);
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
    let url = '/api/photo/tree?type=my';
    if (id !== 0 && !isNaN(id)) {
        url = '/api/photo/tree?type=my&&id=' + id;
    }
    $.ajax(url, {
        method: 'GET',
        dataType: 'json',
        success: (data) => {
            updateBreadCrumb(data[0]);
            updateTree(data[1], data[2]);
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

$('#openEditAlbumModal').click(() => {
    $.ajax('/api/photo/album/get', {
        method: 'GET',
        dataType: 'json',
        success: (albums) => {
            const editAlbumForm = $('#editAlbumForm');
            editAlbumForm.find('#albumParent option').remove();
            editAlbumForm.find('#albumParent').append("<option value='0'>Нет родителя</option>");
            albums.forEach((album) => {
                editAlbumForm.find('#albumParent').append("<option value='" + album.id + "'>" + album.name + "</option>");
            })
        }
    })
    editAlbumModalBootstrap.show();
})

$("#editAlbumSubmit").click(() => {
    const editAlbumForm = $('#editAlbumForm');
    const id = editAlbumForm.find('[name="id"]').val();
    const ownerID = editAlbumForm.find('[name="ownerID"]').val();
    const name = editAlbumForm.find('[name="name"]').val();
    const description = editAlbumForm.find('[name="description"]').val();
    const parentID = editAlbumForm.find('[name="parent_id"]').val();
    const toastDanger = $('#toastDanger');
    if (!$.trim(name).length) {
        toastsBodyClear();
        toastDanger.find('.toast-body').html('Заполните поле "Название альбома"');
        const toast = new bootstrap.Toast(toastDanger, {});
        toast.show();
        return;
    }

    $.ajax('/api/photo/album/' + id, {
        method: 'POST',
        dataType: 'json',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            id,
            ownerID,
            name,
            description,
            parentID
        }),
        success: (req) => {
            toastsBodyClear();
            const toastSuccess = $('#toastSuccess');
            toastSuccess.find('.toast-body').html('Альбом успешно изменен');
            const toast = new bootstrap.Toast(toastSuccess, {});
            toast.show();
            $('#albumParentID').val(req.parentID);
            $('#cardParent').html("Родитель: " + req.parentName);
            $('#albumName').val(req.name);
            $('#cardName').html("Название: " + req.name);
            $('#albumDescription').val(req.description);
            $('#cardDescription').html("Описание: " + req.description);
            $('#cardDateEdit').html("Изменена: " + req.dateEdit);
            editAlbumModalBootstrap.hide();
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

$('#openEditAlbumViewersModal').click(() => {
    const albumID = $('#cardId').val();
    $.ajax('/api/photo/album/' + albumID + '/viewers', {
        method: 'GET',
        dataType: 'json',
        success: (viewers) => {
            const editAlbumViewersForm = $('#editViewersForm');
            updateViewers(editAlbumViewersForm, albumID, viewers)
        }
    })
    editAlbumViewersModalBootstrap.show();
})

$('#viewers_container').on('click', '.addViewer', () => {
    const container = $('#viewers_container');
    const clone = $('#editViewersForm').find('#viewer_item_temp').clone();
    clone.removeAttr('id');
    clone.removeClass('visually-hidden');
    clone.find('select').attr('name', 'viewers');
    clone.find('button').remove();
    clone.append('<button class="btn btn-danger removeViewer" type="button">-</button>')
    container.append(clone);
})

$('#viewers_container').on('click', '.removeViewer', (e) => {
    const btn = $(e.target);
    btn.parent().remove();
})

$('#editAlbumViewersSubmit').click(() => {
    const form = $('#editViewersForm');
    const viewers = form.find('[name = "viewers"]');
    const id = form.find('[name = "id"]').val();
    const data = [];
    viewers.each((key, el) => {
        data.push({
            "albumID": id,
            "viewer": $(el).val()
        });
    })
    $.ajax('/api/photo/album/' + id + '/viewers', {
        method: 'POST',
        dataType: 'json',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(data),
        success: (viewers) => {
            const editAlbumViewersForm = $('#editViewersForm');
            updateViewers(editAlbumViewersForm, id, viewers);
            toastsBodyClear();
            const toastSuccess = $('#toastSuccess');
            toastSuccess.find('.toast-body').html('Доступ к альбому изменен');
            const toast = new bootstrap.Toast(toastSuccess, {});
            toast.show();
            editAlbumViewersModalBootstrap.hide();
        }
    });
})

$('#openEditPhotoViewersModal').click(() => {
    const photoID = $('#cardId').val();
    $.ajax('/api/photo/' + photoID + '/viewers', {
        method: 'GET',
        dataType: 'json',
        success: (viewers) => {
            const editPhotoViewersForm = $('#editViewersForm');
            updateViewers(editPhotoViewersForm, photoID, viewers)
        }
    })
    editPhotoViewersModalBootstrap.show();
})

$('#editPhotoViewersSubmit').click(() => {
    const form = $('#editViewersForm');
    const viewers = form.find('[name = "viewers"]');
    const id = form.find('[name = "id"]').val();
    const data = [];
    viewers.each((key, el) => {
        data.push({
            "photoID": id,
            "viewer": $(el).val()
        });
    })
    $.ajax('/api/photo/' + id + '/viewers', {
        method: 'POST',
        dataType: 'json',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(data),
        success: (viewers) => {
            const editPhotoViewersForm = $('#editViewersForm');
            updateViewers(editPhotoViewersForm, id, viewers);
            toastsBodyClear();
            const toastSuccess = $('#toastSuccess');
            toastSuccess.find('.toast-body').html('Доступ к фотографии изменен');
            const toast = new bootstrap.Toast(toastSuccess, {});
            toast.show();
            editPhotoViewersModalBootstrap.hide();
        }
    });
})
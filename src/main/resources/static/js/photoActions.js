const createCategoryModalBootstrap = $('#createCategoryModal').length != 0 ? new bootstrap.Modal($('#createCategoryModal'), {}) : null;
const createPhotoModalBootstrap = $('#createPhotoModal').length != 0 ? new bootstrap.Modal($('#createPhotoModal'), {}) : null;
const editPhotoModalBootstrap = $('#editPhotoModal').length != 0 ? new bootstrap.Modal($('#editPhotoModal'), {}) : null;
const editCategoryModalBootstrap = $('#editCategoryModal').length != 0 ? new bootstrap.Modal($('#editCategoryModal'), {}) : null;

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

const toastsBodyClear = () => {
    $('#toastDanger').find('.toast-body').html('');
    $('#toastSuccess').find('.toast-body').html('');
}

$('#openCreateCategoryModal').click(() => {
    $.ajax('/api/photo/category/get', {
        method: 'GET',
        dataType: 'json',
        success: (categories) => {
            const createCategoryForm = $('#createCategoryForm');
            createCategoryForm.find('#categoryName').val('');
            createCategoryForm.find('#categoryDescription').val('');
            createCategoryForm.find('#categoryParent option').remove();
            createCategoryForm.find('#categoryParent').append("<option value='0'>Нет родителя</option>");
            categories.forEach((category) => {
                createCategoryForm.find('#categoryParent').append("<option value='" + category.id + "'>" + category.name + "</option>");
            })
        }
    })
    createCategoryModalBootstrap.show();
})

$("#createCategorySubmit").click(() => {
    const createCategoryForm = $('#createCategoryForm');
    const name = createCategoryForm.find('[name="name"]').val();
    const description = createCategoryForm.find('[name="description"]').val();
    const parentID = createCategoryForm.find('[name="parent_id"]').val();
    const toastDanger = $('#toastDanger');
    if (!$.trim(name).length) {
        toastsBodyClear();
        toastDanger.find('.toast-body').html('Заполните поле "Название категории"');
        const toast = new bootstrap.Toast(toastDanger, {});
        toast.show();
        return;
    }

    $.ajax('/api/photo/category', {
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
            createCategoryModalBootstrap.hide();
            location.reload();
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
    $.ajax('/api/photo/category/get', {
        method: 'GET',
        dataType: 'json',
        success: (categories) => {
            const createPhotoForm = $('#createPhotoForm');
            createPhotoForm.find('#photoFile').val('');
            createPhotoForm.find('#photoName').val('');
            createPhotoForm.find('#photoDescription').val('');
            createPhotoForm.find('#photoCategory option').remove();
            createPhotoForm.find('#photoCategory').append("<option value='0'>Нет категории</option>");
            categories.forEach((category) => {
                createPhotoForm.find('#photoCategory').append("<option value='" + category.id + "'>" + category.name + "</option>");
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
    const categoryID = createPhotoForm.find('[name="categoryID"]').val();
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
    formData.append('categoryID', categoryID);
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

$('#openEditCategoryModal').click(() => {
    $.ajax('/api/photo/category/get', {
        method: 'GET',
        dataType: 'json',
        success: (categories) => {
            const editCategoryForm = $('#editCategoryForm');
            editCategoryForm.find('#categoryParent option').remove();
            editCategoryForm.find('#categoryParent').append("<option value='0'>Нет родителя</option>");
            categories.forEach((category) => {
                editCategoryForm.find('#categoryParent').append("<option value='" + category.id + "'>" + category.name + "</option>");
            })
        }
    })
    editCategoryModalBootstrap.show();
})

$("#editCategorySubmit").click(() => {
    const editCategoryForm = $('#editCategoryForm');
    const id = editCategoryForm.find('[name="id"]').val();
    const ownerID = editCategoryForm.find('[name="ownerID"]').val();
    const name = editCategoryForm.find('[name="name"]').val();
    const description = editCategoryForm.find('[name="description"]').val();
    const parentID = editCategoryForm.find('[name="parent_id"]').val();
    const toastDanger = $('#toastDanger');
    if (!$.trim(name).length) {
        toastsBodyClear();
        toastDanger.find('.toast-body').html('Заполните поле "Название категории"');
        const toast = new bootstrap.Toast(toastDanger, {});
        toast.show();
        return;
    }

    $.ajax('/api/photo/category/' + id, {
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
            toastSuccess.find('.toast-body').html('Категория успешно изменена');
            const toast = new bootstrap.Toast(toastSuccess, {});
            toast.show();
            $('#categoryParentID').val(req.parentID);
            $('#cardParent').html("Родитель: " + req.parentName);
            $('#categoryName').val(req.name);
            $('#cardName').html("Название: " + req.name);
            $('#categoryDescription').val(req.description);
            $('#cardDescription').html("Описание: " + req.description);
            $('#cardDateEdit').html("Изменена: " + req.dateEdit);
            editCategoryModalBootstrap.hide();
        },
        error: (req) => {
            toastsBodyClear();
            toastDanger.find('.toast-body').html(req.status);
            const toast = new bootstrap.Toast(toastDanger, {});
            toast.show();
        }
    })
})

$('.openEditPhotoModal').click(() => {
    $.ajax('/api/photo/category/get', {
        method: 'GET',
        dataType: 'json',
        success: (categories) => {
            const editPhotoForm = $('#editPhotoForm');
            const currentcategoryID = editPhotoForm.find('#photoCategoryID').val();
            editPhotoForm.find('#photoCategory option').remove();
            editPhotoForm.find('#photoCategory').append("<option value='0'>Нет категории</option>");
            categories.forEach((category) => {
                const selected = category.id === parseInt(currentcategoryID) ? 'selected' : '';
                editPhotoForm.find('#photoCategory').append("<option value='" + category.id + "'" + selected + ">" + category.name + "</option>");
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
    const categoryID = editPhotoForm.find('[name="categoryID"]').val();
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
    formData.append('categoryID', categoryID);
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
            $('#photoCategoryID').val(req.categoryID);
            $('#cardCategory').html("Альбом: " + req.categoryName);
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

const updateComments = (comments) => {
    const tempComment = $(`<div class="d-flex flex-start mb-4 photo_comment-item">
                            <img class="rounded-circle shadow-1-strong me-3"
                                 src="" alt="avatar" width="65"
                                 height="65"/>
                            <div class="card w-100">
                                <div class="card-body p-4">
                                    <h5></h5>
                                    <p class="small"></p>
                                    <p class="text-comment"></p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <button type="button" data-id=""
                                                class="removeComment btn btn-outline-danger"><i
                                                class="fa-solid fa-minus"></i></button>
                                    </div>
                                </div>
                            </div>
                        </div>`);
    const container = $('#photo_comments');
    container.find('.photo_comment-item').remove();
    const currentUserID = localStorage.getItem("currentUserID");
    if (comments.length == 0) {
        container.append(`<div class="card mb-4 photo_comment-item">
                                <div class="card-body">
                                    Пока нет комментариев
                                </div>
                            </div>`)
    } else {
        comments.forEach((comment) => {
            const cloneComment = tempComment.clone();
            cloneComment.find('img').attr('src', comment.user.avatar);
            cloneComment.find('h5').text(comment.user.fio);
            cloneComment.find('.small').text(comment.dateAdd);
            cloneComment.find('.text-comment').text(comment.comment);
            if (comment.userID !== parseInt(currentUserID)) {
                cloneComment.find('.removeComment').parent().remove();
            } else {
                cloneComment.find('.removeComment').attr('data-id', comment.id);
            }
            container.append(cloneComment);
        })
    }
}

$('#addedComment').click(() => {
    const comment = $('#addComment').val();
    const photoID = $('#cardId').val();
    $.ajax('/api/photo/comment', {
        method: 'POST',
        dataType: 'json',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            photoID,
            comment
        }),
        success: (data) => {
            updateComments(data);
            $('#addComment').val('');
        }
    })
})

$('#photo_comments').click('.removeComment', (e) => {
    let commentID = $(e.target).attr("data-id");
    if (!parseInt(commentID)) {
        commentID = $(e.target).parent().attr("data-id");
    }
    const photoID = $('#cardId').val();
    $.ajax('/api/photo/' + photoID + '/comment/' + commentID, {
        method: 'GET',
        dataType: 'json',
        success: (data) => {
            updateComments(data);
        }
    })
})
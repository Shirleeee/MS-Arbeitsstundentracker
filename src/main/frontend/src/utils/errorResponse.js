export const handleErrorResponse = (error,errors) => {
    if (error.response && error.response.data) {
        console.log("error.response.data", error.response.data);
        error.response.data.forEach(err => {
            if (err.field) {
                console.log("err", err);
               return errors.value[err.field] = err.message;
            }
        });
    } else {
        console.error('Error creating project', error);
    }
    return errors;
};
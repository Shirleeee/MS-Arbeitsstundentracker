export const handleErrorResponse = (error, errors) => {
    if (error.response?.data) {
        console.log("error.response.data", error.response.data);

        error.response.data.forEach(err => {
            if (err && typeof err === "object" && "field" in err) {
                console.log("err", err);
                errors.value[err.field] = err.message;
            }
        });
    } else {
        console.error('Error creating project', error);
    }
    return errors;
};

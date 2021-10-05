import axios from "axios";

export const getTodos = () => {
    return axios.get("/api/todo")
        .then(res => res.data)
        .catch(console.error);
}

export const postTodo = (description) => {

    const newTodo = {
        description : description,
        status : "OPEN"
    }

    return axios.post("api/todo", newTodo)
        .then(res => res.data)
}

export const putTodo = (todo) => {
    return axios.put(`api/todo/${todo.id}`, todo)
        .then(res => res.data)
}

export const deleteTodo = (id) => {
    return axios.delete(`api/todo/${id}`);
}



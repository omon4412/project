import axios from "axios";
import store from "@/store";

export default function checkSession() {
    return axios.get('http://localhost:5100/api/v1/user', {withCredentials: true})
        .then(response => {
            console.log(response.data)
            store.commit('updateUser', response.data);
            return response.data;
        })
        .catch((error) => {
            console.log(error);
            store.commit('updateUser', null);
            return '';
        })
}
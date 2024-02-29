import axios from "axios";
import store from "@/store";

export default function checkSession() {
   return axios.get('http://localhost:5100/api/v1/user', {withCredentials: true})
        .then(response => {
            store.commit('updateUsername', response.data.username);
            return response.data.username;
        })
        .catch((error) => {
            console.log(error);
            store.commit('updateUsername', '');
            return '';
        })
}
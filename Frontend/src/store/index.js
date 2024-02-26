import {createStore} from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
    state: {
        username: ''
    },
    getters:{
        getUsername(state){
            return state.username;
        }
    },
    mutations: {
        updateUsername (state, newUsername) {
            state.username = newUsername
        },
    },
    plugins: [createPersistedState()]
});

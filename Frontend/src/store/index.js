import {createStore} from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
    state: {
        userData: null,
        adminOpenPage: null
    },
    getters: {
        getUser(state) {
            return state.userData;
        },
        getAdminOpenPage(state) {
            return state.adminOpenPage;
        }
    },
    mutations: {
        updateUser(state, newUserData) {
            state.userData = newUserData
        },
        updateAdminOpenPage(state, newAdminOpenPage) {
            state.adminOpenPage = newAdminOpenPage
        },
    },
    plugins: [createPersistedState()]
});

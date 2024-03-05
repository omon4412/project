import {createApp} from 'vue'
import App from "./App.vue";
import components from "@/components/UI"
import router from "@/router/router";
import {library} from "@fortawesome/fontawesome-svg-core";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {faEye} from "@fortawesome/free-solid-svg-icons/faEye";
import {faEyeSlash, faNavicon} from "@fortawesome/free-solid-svg-icons";
import 'bootstrap/dist/css/bootstrap.css'
import bootstrap from 'bootstrap/dist/js/bootstrap'
import store from '@/store';

const app = createApp(App);
components.forEach(component => {
    app.component(component.name, component)
})

library.add(faEye, faEyeSlash, faNavicon);

app.use(router).use(store).use(bootstrap).component("font-awesome-icon", FontAwesomeIcon)
    .mount('#app')
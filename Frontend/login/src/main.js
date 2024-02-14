import {createApp} from 'vue'
import App from "./App.vue";
import components from "@/components/UI"
import router from "@/router/router";
import { library } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import {faEye} from "@fortawesome/free-solid-svg-icons/faEye";
import {faEyeSlash} from "@fortawesome/free-solid-svg-icons";

const app = createApp(App);
components.forEach(component => {
    app.component(component.name, component)
})

library.add(faEye, faEyeSlash);


app.use(router).component("font-awesome-icon", FontAwesomeIcon)
    .mount('#app')
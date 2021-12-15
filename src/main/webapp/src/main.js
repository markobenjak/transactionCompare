import Vue from 'vue'
import App from './App.vue'

import { BootstrapVue, IconsPlugin, NavbarPlugin } from 'bootstrap-vue'

// Import Bootstrap an BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import axios from 'axios'
// Make BootstrapVue available throughout your project
Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin)
Vue.use(NavbarPlugin)
import Vuelidate from 'vuelidate'
Vue.use(Vuelidate)

const getRuntimeConfig = async () => {
   const runtimeConfig = await fetch('config.json');
   return await runtimeConfig.json()
}
getRuntimeConfig().then(function(json) {
  axios.defaults.baseURL = json.WEB_SERVICE_URL;
});
new Vue({
    render: h => h(App)
}).$mount('#app')
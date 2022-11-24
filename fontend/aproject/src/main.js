import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index'
import datePicker from '@vuepic/vue-datepicker';
import store from './store';

createApp(App)
.use(store)
.use(router)
.component('Datepicker',datePicker)
.mount('#app')

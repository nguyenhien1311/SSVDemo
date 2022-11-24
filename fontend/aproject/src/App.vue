<template>
   <div class="app">
    <MenuBar />
    <router-view />
   </div>
</template>

<script>
import MenuBar from './view/Menu.vue'

export default {
    name: 'app',
    components: {
        MenuBar
    }
}
</script>

<style>
#app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
}
</style>

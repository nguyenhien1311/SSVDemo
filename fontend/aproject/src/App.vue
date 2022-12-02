<template>
   <div class="app container">
    <MenuBar v-if="this.$store.state.isLogin && this.$store.state.authorities != 'ROLE_USER'"/>
    <!-- && this.$store.state.authorities.some(role => role === 'ROLE_ADMIN' ||  'ROLE_SUPERVISOR') -->
    <MenuUser v-if="this.$store.state.isLogin && this.$store.state.authorities == 'ROLE_USER'" />
    <router-view />
   </div>
</template>

<script>
import MenuBar from './view/Menu.vue'
import MenuUser from './view/MenuUser.vue';
export default {
    name: 'app',
    components: {
        MenuBar,
        MenuUser
    },
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

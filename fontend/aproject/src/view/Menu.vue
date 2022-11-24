<template>
    <div class="MenuBar">
        <nav class="navbar navbar-light bg-light">
        <div class="container-fluid">
            <div class="d-flex align-items-center">
                <router-link to="/"  class=" me-3" >Student</router-link>
                <router-link to="/class" class=" me-3" >Class</router-link>
                <router-link to="/monhoc" class=" me-3">Subject</router-link>
            </div>
            <div class="d-flex align-items-center">
                <label class=" px-3 me-2">
                Hello , {{this.$store.state.userName}}
                </label>
                <button type="button" class="btn btn-primary me-3" @click="logOut()">
                logout
                </button>
            </div>
        </div>
        </nav>
    </div>
</template>
<script>
import axios from 'axios';
export default {
    name: 'MenuBar',
    methods : {
        logOut(){
            alert("Logout success ")
            axios.get('http://localhost:8080/api/v1/logout')
            this.$store.commit('logout')
            window.location.reload();
        }
    }
}
</script>

<style>
.menu {
    display: flex;
    justify-content: center;
}

.item-menu {
    margin: 10px;
    font-size: large;
    cursor: pointer;
    font-weight: 600;
}

.link {
    color: inherit;
    text-decoration: none;
}
</style>
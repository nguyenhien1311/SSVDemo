<template>
    <div class="MenuBar" v-if="this.$store.state.isAuthorited">
        <nav class="navbar navbar-light" style="background-color: #e3f2fd;">
        <div class="container-fluid"   >
            <div class="d-flex align-items-center" >
                <router-link to="/"  class="btn btn-primary me-3" >User</router-link>
                <router-link to="/class" class="btn btn-primary me-3" >Class</router-link>
                <router-link v-if="this.$store.state.authorities != 'ROLE_SUPERVISOR'" to="/monhoc" class="btn btn-primary me-3">Subject</router-link>
               
            </div>
            <div class="d-flex align-items-center">
                <label class=" px-3 me-2">
                <router-link to="/detail">Hello , {{this.$store.state.userName}}</router-link>
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
            this.$router.push('/login');
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
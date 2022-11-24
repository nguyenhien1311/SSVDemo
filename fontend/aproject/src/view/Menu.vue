<template>
    <div class="MenuBar">
    
        <nav class="navbar bg-light">
    
            <form class="container-fluid justify-content-start">
    
                <button class="btn btn-outline-success me-2" type="button"><router-link to="/">Student</router-link><br></button>
    
                <button class="btn btn-outline-success me-2" type="button"> <router-link to="/class">Class</router-link></button>
                
                <button class="btn btn-outline-success me-2" type="button"> <router-link to="/monhoc">Subject</router-link></button>
                <button v-if="this.$store.state.userName == null || this.$store.state.userName == ''" class="btn btn-outline-success me-2" type="button"> <router-link to="/login">Login</router-link></button>
                <button v-else class="btn btn-outline-success me-2" type="button" @click="logOut()"> Logout</button>
                <!-- <button v-if="this.userName == null || this.userName == ''" class="btn btn-outline-success me-2" type="button"> <router-link to="/register">Register</router-link></button> -->
                <p v-if="this.$store.state.userName !=null && this.$store.state.userName != ''">Hello, {{this.$store.state.userName }}</p>
            </form>
            
        </nav>

    </div>
</template>
<script>
import axios from 'axios';
export default {
    name: 'MenuBar',
    data(){
        return {
       };
    },
    created() {
        
    },
    methods : {
        logOut(){
            alert("Logout success ")
            axios.get('http://localhost:8080/api/v1/logout')
            this.$store.commit('logout')
            this.$router.push("/login");
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
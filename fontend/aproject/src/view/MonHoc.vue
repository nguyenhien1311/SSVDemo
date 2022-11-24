<template>
    <div id="app">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <form class="d-flex">
                        <input class="form-control" type="search" @keypress="search(key)" v-model="key" placeholder="Enter subject name to search" aria-label="Search">
                        
                    </form>
                    <button class="btn btn-outline-success" type="button" data-bs-toggle="modal" data-bs-target="#exampleModal" style="margin-left: 20px;">Add Subject</button>
                </div>
            </div>
        </nav>
        
        <br>
        <div class="tabla-usuarios">
            <br>
            <label><h2>List Subject</h2></label>
            <table class="table table-hover table-striped table-active">
                <thead>
                    <th>NO</th>
                    <th>Name</th>
                    <th>Status</th>
                    <th>Action</th>
                </thead>
                <tbody>
                    <tr v-for="(s, index) in subjects" v-bind:key="s.id">
                        <td>{{ index +1 }}</td>
                        <td>{{ s.subjectName }}</td>
                        <td>{{ s.status?'Normal':'Stopped' }}</td>
                        <td >
                            <div v-if="s.status">
                                <button class="btn btn-primary" type="submit" @click="openDetailForm(s.id)" data-bs-toggle="modal" data-bs-target="#detailModal"  style="margin-left: 20px;">Detail</button>
                                <button class="btn btn-primary" type="submit" @click="openEditForm(s.id)" data-bs-toggle="modal" data-bs-target="#exampleModal"  style="margin-left: 20px;">Edit</button>
                                <button class="btn btn-warning" type="submit" @click="deleteSubject(s.id)"  style="margin-left: 20px;">Stop</button>
                             </div>
                             <div v-else>
                                <button class="btn btn-success" type="submit" @click="deleteSubject(s.id)"  style="margin-left: 20px;">Continue</button>
                             </div>
                             
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Subject Information</h5>
                        <button id="closeModal" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveSubject()">
                            <div class="form-group row">
                                <input type="hidden" v-model="id">
                                <input ref="name" class="form-control col-sm-8" placeholder="Subject Name" type="text" v-model="state.name" />
                                <span v-if="v$.name.$error" style="color: red;">{{v$.name.$errors[0].$message}}</span>
                            </div>
                            <div class="form-group row container">
                                <input class="btn btn-primary col col-sm-6" type="submit" value="Save">
                                <input class="btn btn-danger col col-sm-6" type="reset" value="Reset" @click="clearData()">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="detailModal" tabindex="-1" aria-labelledby="classModalLabel" aria-hidden="true">
<div class="modal-dialog modal-lg">
  <div class="modal-content">
    <div class="modal-header float-right">
      <h5>Subject Information</h5>
      <div class="text-right">
        <button id="closeModal" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
    </div>
            <div class="modal-body">
                <div class="input-group flex-nowrap" style="margin-bottom: 20px;">
                    <span class="input-group-text" id="addon-wrapping">Id</span>
                    <input type="text" readonly class="form-control" :placeholder="id" aria-describedby="addon-wrapping" disabled>
                </div>
                <div class="input-group flex-nowrap" style="margin-bottom: 20px;">
                    <span class="input-group-text" id="addon-wrapping2">Name</span>
                    <input type="text" readonly class="form-control" :placeholder="state.name" aria-describedby="addon-wrapping2" disabled>
                </div>
                <br>
                    <table class="table table-bordered">
                        <thead>
                        <th>NO</th>
                        <th>Code</th>
                        <th>Class Name</th>
                        <th>Status</th>
                    </thead>
                    <tbody v-if="classes.length > 0">
                        <tr v-for="(c, index) in classes" :key="c.id">
                            <td>{{ index +1 }}</td>
                            <td>{{ c.code }}</td>
                            <td>{{ c.className }}</td>
                            <td>{{ c.status }}</td>
                        </tr>
                    </tbody>
                    <tbody v-else>
                        <tr>
                            <td colspan="4">No Class</td>
                        </tr>
                    </tbody>
                </table>
            </div>
    </div>
  </div>
</div>
    <pagination v-model="page" :records="listings.total" :per-page="listings.per_page" @paginate="paginate" />
</template>

<script>
import axios from 'axios'
import Pagination from 'v-pagination-3'

import { reactive,computed } from 'vue'
import useVuelidate from '@vuelidate/core'
import {required,minLength,maxLength} from '@vuelidate/validators'

const url = 'http://localhost:8080/api/v1/subject/'
export default {
    setup(){
        const state  = reactive({
            name: '',
        })

        const rules = computed(()=>{
            return {
                name:{
                required,
                minLength:minLength(1),
                maxLength: maxLength(40),
            }
        }
        })

        const v$ = useVuelidate(rules, state)

        return {
            state,
            v$
        }
    },
    name: 'app',
    components: {
        Pagination: Pagination
    },
    data() {
        return {
            subjects: [],
             classes: [],
             id: '',
             key:'',
            page: 1,
            listings: {
                total: 0,
                per_page: 0
            },
            // reg: {
            //     email: /^(.+)@(.+)$/,
            //     text: /^[a-zA-Z ]*$/,
            //     phone: /(84|0[3|5|7|8|9])+([0-9]{8})\b/
            // }
        }
    },
    created() {
        this.getSubjects()
    },
    methods: {
       async getSubjects(){
        const response = await axios.get(url,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
            const data = await response.data.body.pages
            this.loadSubjects(data.content)
            this.logData(data)
       },
      loadSubjects(data) {
        this.subjects = data
      },
     async search(key){
       const response = await  axios.get(url + '?key=' + key,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
       const data = await response.data.body.pages.content
       this.subjects = data
        this.listings.total = data.length
      },
      async openDetailForm(id){
           const response = await axios.get(url+id+'/classes',{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
           const data = response.data.body
           this.openEditForm(id)
           this.classes = data.list
      },
      logData(data) {
            this.page = data.pageable.pageNumber + 1
            this.listings.total = data.totalElements
            this.listings.per_page = data.pageable.pageSize
        },
      
      paginate(e) {
            axios
                .get(url + '?skip=' + (e - 1),{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
                .then(response => (this.subjects = response.data.body.pages.content))
        },
      saveSubject(){
            this.v$.$validate();
            if(!this.v$.$error){
                if (this.id != null && this.id !== '') {
                axios.put(url + this.id, {'name': this.state.name },{
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization':'Bearer ' + this.$store.state.token
                                }}).then(response => (alert(response.data.message)))
                                .finally(   
                                    window.location.reload()
                        )
                            
                    } else{
                    axios.post(url, {'name': this.state.name },{
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization':'Bearer ' + this.$store.state.token
                                }}).then(response => (alert(response.data.message)))
                                .finally(   
                                    window.location.reload()
                        )
                    }
            }
      },
      async openEditForm(id) {
          const response = await axios.get(url+id,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
          const data = await response.data.body
          this.loadSubjectDetail(data)
      },
      loadSubjectDetail(data){
        this.id = data.id
        this.state.name = data.name
      },
      async deleteSubject(id){
           const response = await axios.patch(url+ id,null,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
            const msg = await response.data.message
            if(confirm(msg)){
                window.location.reload()
            }
      },
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

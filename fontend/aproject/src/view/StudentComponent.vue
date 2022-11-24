<template>
    <div id="StudentCpn">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <form class="d-flex">
                        <input class="form-control" type="search" @keypress="search(key)" v-model="key" placeholder="Enter student name to search" aria-label="Search">
                        
                    </form>
                    <button class="btn btn-outline-success" type="button" data-bs-toggle="modal" data-bs-target="#exampleModal" style="margin-left: 20px;">Add Student</button>
                </div>
            </div>
        </nav>
        
        <br>
        <div class="tabla-usuarios">
            <br>
            <label><h2>List Student</h2></label>
            <table class="table table-hover table-striped table-active">
                <thead>
                    <th>NO</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Status</th>
                    <th>Action</th>
                </thead>
                <tbody>
                    <tr v-for="(s, index) in students" :key="s.id">
                        <td>{{ index +1 }}</td>
                        <td>{{ s.name }}</td>
                        <td>{{ s.email }}</td>
                        <td>{{ s.phone }}</td>
                        <td>{{ s.status?'ACTIVE':'INACTIVE' }}</td>
                        <td v-if="s.status" >
                            <button class="btn btn-primary" type="submit" @click="openDetail(s.id)" data-bs-toggle="modal" data-bs-target="#detailModal"  style="margin-left: 20px;">Detail</button>
                            <button class="btn btn-primary" type="submit" @click="openEditForm(s.id)" data-bs-toggle="modal" data-bs-target="#exampleModal"  style="margin-left: 20px;">Edit</button>
                            <button v-if="!s.inClass" class="btn btn-success" type="submit" @click="openClassModal(s.id)" data-bs-toggle="modal" data-bs-target="#classModal"  style="margin-left: 20px;">Join class</button>
                            <button class="btn btn-warning" type="submit" @click="deleteStudent(s.id)"  style="margin-left: 20px;">Delete</button>
                        </td>
                        <td v-else >
                            <button class="btn btn-success" type="submit" @click="exportData(s.id)"  style="margin-left: 20px;">Export</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Student Information</h5>
                        <button id="closeModal" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="saveStudent()">
                            <div class="form-group row">
                                <input type="hidden" v-model="id">
                                <input ref="name" class="form-control col-sm-8" placeholder="Full Name" type="text" v-model="state.name" />
                                <span v-if="v$.name.$error" style="color: red;">{{v$.name.$errors[0].$message}}</span>
                                <input ref="email" class="form-control col-sm-8" placeholder="Email" type="text" v-model="state.email" />
                                <span v-if="v$.email.$error" style="color: red;">{{v$.email.$errors[0].$message}}</span>
                                <input ref="phone" class="form-control col-sm-8" placeholder="Phone Number" type="text" v-model="state.phone" />
                                <span v-if="v$.phone.$error" style="color: red;">{{v$.phone.$errors[0].$message}}</span>
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

        <div class="modal fade" id="classModal" tabindex="-1" aria-labelledby="classModalLabel" aria-hidden="true">
<div class="modal-dialog">
  <div class="modal-content">
    <div class="modal-header float-right">
      <h5>List class available</h5>
      <div class="text-right">
        <i data-dismiss="modal" aria-label="Close" class="fa fa-close"></i>
      </div>
    </div>
        <form @submit.prevent="join()">
            <div class="modal-body">
                    <select class="form-control col-sm-8" v-model="classId" required>
                        <option value="" disabled hidden>Select a class to join</option>
                        <option v-for="s in classes" v-bind:key="s.id" :value="s.id">{{ s.className }}</option>
                    </select>
                <button type="submit" class="btn btn-primary">Join</button>
            </div>
        </form>
    </div>
  </div>
</div>
    </div>
    <div class="modal fade" id="detailModal" tabindex="-1" aria-labelledby="classModalLabel" aria-hidden="true">
<div class="modal-dialog modal-lg">
  <div class="modal-content">
    <div class="modal-header float-right">
      <h5>Student Detail</h5>
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
                    <span class="input-group-text" id="addon-wrapping1">Name</span>
                    <input type="text" readonly class="form-control" :placeholder="state.name" aria-describedby="addon-wrapping1" disabled>
                </div>
                <div class="input-group flex-nowrap" style="margin-bottom: 20px;">
                    <span class="input-group-text" id="addon-wrapping2">Email</span>
                    <input type="text" readonly class="form-control" :placeholder="state.email" aria-describedby="addon-wrapping2" disabled>
                </div>
                <div class="input-group flex-nowrap" style="margin-bottom: 20px;">
                    <span class="input-group-text" id="addon-wrapping3">Phone</span>
                    <input type="text" readonly class="form-control" :placeholder="state.phone" aria-describedby="addon-wrapping3" disabled>
                </div>
                <br>
                    <table class="table table-bordered">
                        <thead>
                        <th>NO</th>
                        <th>Class Name</th>
                        <th>Mark</th>
                        <th>Status</th>
                        <th>Action</th>
                    </thead>
                    <tbody>
                        <tr v-for="(p, index) in papers" :key="p.id">
                            <td>{{ index +1 }}</td>
                            <td>{{ p.className }}</td>
                            <td>{{ p.mark==null?'0':p.mark }}</td>
                            <td>{{ p.isPass==null?'Not complete':p.isPass?'Passed':'Failed' }}</td>
                            <td v-if="p.isPass==null"> 
                                <form @submit.prevent="saveMark()">
                                    <input id="paperId" type="hidden" :value="p.id">
                                    <input type="number" min="0" max="10" class="form-control mb-2 mr-sm-2" id="inlineFormInputName2" v-model="mark" >
                                    <button type="submit" class="btn btn-primary mb-2">Submit</button>
                                </form>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button class="btn btn-success" type="submit" @click="exportData()"  style="margin-left: 20px;">Export</button>
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
import {required, email,minLength,maxLength,helpers} from '@vuelidate/validators'


const paperUrl= 'http://localhost:8080/api/v1/paper/'
const url = 'http://localhost:8080/api/v1/student/'


export default {
    setup(){
        const state  = reactive({
            name: '',
            email: '',
            phone: '',
        })
        
         const onlyDigits = /^[a-zA-Z ]*$/;
         const phoneNumbers = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;

        const isNameValid = (value) => onlyDigits.test(value)
        const isPhoneValid = (value) => phoneNumbers.test(value)

        const rules = computed(()=>{
            return {
            name:{
                required,
                minLength:minLength(5),
                maxLength: maxLength(40),
                isNameValid: helpers.withMessage('Name must be only digits',isNameValid)
            },
            email:{required,email},
            phone:{
                required,
                minLength:minLength(10),
                maxLength: maxLength(10),
                isPhoneValid: helpers.withMessage('Phone number not valid ! \n-start with 84|0[3|5|7|8|9]',isPhoneValid)
            }
        }
        })

        const v$ = useVuelidate(rules, state)

        return {
            state,
            v$
        }
    },
    name: 'StudentCpn',
    components: {
        Pagination: Pagination
    },
    data() {
        return {
            students: [],
             classes: [],
             papers:[],
             paper:'',
             paperId: 0,
             currentId: '',
             mark:'',
            id: '',
            classId: '',
            className: '',
            key: '',
            page: 1,
            listings: {
                total: 0,
                per_page: 0
            },
        }
    },
    validations() {
       
    },
    created() {
        this.getListStudent()
        this.loadClasses()
    },
    methods: {
        async getListStudent() {
            const response = await axios.get(url,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
            const data = await response.data.body.pages
            this.loadStudent(data.content)
            this.logData(data)
        },
        loadClasses() {
            axios.get('http://localhost:8080/api/v1/class/data',{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }}).then(response => (this.classes = response.data.body.list))
        },
        paginate(e) {
            axios
                .get(url + '?skip=' + (e - 1),{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
                .then(response => (this.students = response.data.body.pages.content))
        },
        logData(data) {
            this.page = data.pageable.pageNumber + 1
            this.listings.total = data.totalElements
            this.listings.per_page = data.pageable.pageSize
        },
        loadStudent(data) {
            this.students = data
           
        },
        saveMark(){
            axios.put(paperUrl + document.getElementById('paperId').value , { 'mark': this.mark},{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }}).then(response => (this.handleData(response.data)))
            this.openDetail(this.currentId);
        },
        async deleteStudent(id) {
            if (confirm('Do you really want to delete this student?')) {
                const response = await axios.patch(url + id,null,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
                this.getListStudent()
                console.log(response.fakedata)
            }
        },
        loadData(data) {
            this.id = data.id
            this.state.name = data.name
            this.state.email = data.email
            this.state.phone = data.phone 
            this.classId = data.classId
            this.className = data.className
        },
        clearData() {
            this.id = ''
            this.state.name = ''
            this.state.email = ''
            this.state.phone = ''
            this.classId = ''
            this.className = ''
        },
       async openDetail(id){
            this.currentId = id
            const fistRequest = await axios.get(url + id,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
            const data = await fistRequest.data.body
            
            const secondRequest = await axios.get(url+id+'/paper',{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
            const papers1 = await secondRequest.data.body.papers

            this.loadData(data)
            this.papers = papers1
        },
        async loadStudentDetail(id) {
            const response = await axios.get(url + id,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
            const data = await response.data.body

            this.loadData(data)
           
        },
        openClassModal(id){
            this.currentId = id
            this.loadClasses()
        },
        exportData(id) {
            const header = {
                            'Authorization':'Bearer ' + this.$store.state.token
                        }
            axios.get(url+ id+'/excel',{
                        headers: header,
                        responseType: 'blob'
                    })
            .then((res) => {
                
                const url = window.URL.createObjectURL(new Blob([res.data]));
                const a = document.createElement("a");
                a.href = url;
                const filename = `file.xlsx`;
                a.setAttribute('download', filename);
                document.body.appendChild(a);
                console.log(res.data)
                a.click();
                a.remove();
            })
            .catch((error) => {
                console.log(error);
            });
        },
        saveStudent() {
            this.v$.$validate();
            if(!this.v$.$error){
                const id = this.id
                // if (!this.reg.text.test(this.name)) {
                //     alert("Name must contain only characters!")
                //     this.$refs.name.value = ''
                //     this.$refs.name.focus()
                //     event.preventDefault()
                // }
                // if (!this.reg.email.test(this.email)) {
                //     alert("Email not valid!")
                //     this.$refs.email.value = ''
                //     this.$refs.email.focus()
                //     event.preventDefault()
                // }
                // if (this.reg.phone.test(this.phone)) {
                //     alert("Phone number not valid ! \n-start with 84|0[3|5|7|8|9]\n-Have 10 number")
                //     this.$refs.phone.value = ''
                //     this.$refs.phone.forcus()
                //     event.preventDefault()
                // }
                if (id != null && id !== '') {
                    axios.put(url + id, { 'name': this.state.name, 'email': this.state.email, 'phone': this.state.phone},{
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization':'Bearer ' + this.$store.state.token
                            }}).then(response => (this.handleData(response.data)))
                        .finally(   
                            this.clearData(),
                            document.getElementById('closeModal').click()
                        )
                } else {
                    axios.post(url, { 'name': this.state.name, 'email': this.state.email, 'phone': this.state.phone},{
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization':'Bearer ' + this.$store.state.token
                            }}).then(response => (this.handleData(response.data)))
                        .finally(
                            this.getListStudent(),
                            document.getElementById('closeModal').click()
                        )
                }
            }
            
        },
        async loadPaper(studentId,classId) {
            const response = await axios.get(url + studentId + '/paper/' + classId,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }})
            const data = response.data
            this.paper = data
            this.paperId = data.id
        },
        handleData(data) {  
            if (data.msg != undefined) {
                alert(data.msg)
            }else if (data == undefined) {
                alert(data)
            }
            
            window.location.reload()
        },
        openEditForm(id) {
            this.loadStudentDetail(id)
            
        },
        search(key) {
            axios.get(url + '?key=' + key,{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }}).then(response => (this.students = response.data.body.pages.content))
        },
        async join() {
            axios.post(paperUrl , { 'studentId': this.currentId,'classId':this.classId },{
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization':'Bearer ' + this.$store.state.token
                        }}).then(response => (this.handleData(response.data)))
        }
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

<template>
    <div id="UserCpn">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <form class="d-flex">
                        <input class="form-control" type="search" v-model="key" placeholder="Enter User name to search"
                            aria-label="Search">
                    </form>
                    <select v-if="this.$store.getters.getAuthorities.includes('ROLE_ADMIN')" id="filter"
                        class="form-control" style="width: 300px;margin-left: 20px;" v-model="roleId"
                        v-on:change="search(key)">
                        <option value="" disabled hidden>Select role</option>
                        <option v-for="r in roles" v-bind:key="r.id" :value="r.id">{{
                                r.roleName == 'ROLE_USER' ? 'Student' : (r.roleName == 'ROLE_ADMIN') ? 'Admin' : 'Teacher'
                        }}</option>
                    </select>
                    <button class="btn btn-success" type="button" style="margin-left: 20px;float:right;"
                        @click="search(key)">Search</button>
                </div>
                <div v-if="this.$store.state.authorities != 'ROLE_SUPERVISOR'">
                <button class="btn btn-primary" type="button" style="float:right;" @click="importFile()">Import</button>
                <input class="file" type="file" @change="changeFile()" ref="file"
                    style="margin-left: 20px;float:right; width: 300px;" />
                </div>

                <button class="btn btn-success" type="button" data-bs-toggle="modal" data-bs-target="#exampleModal"
                    style="margin-left: 20px;float:right;">Add User</button>
            </div>
        </nav>

        <br>
        <div class="tabla-usuarios">
            <br>
            <label>
                <h2>List User</h2>
            </label>
            <table class="table table-hover  table-bordered">
                <thead>
                    <th>NO</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Status</th>
                    <th>Action</th>
                </thead>
                <tbody>
                    <tr v-for="(s, index) in user" :key="s.id">
                        <td>{{ noNum + index }}</td>
                        <td>{{ s.fullName }}</td>
                        <td>{{ s.email }}</td>
                        <td>{{ s.phone }}</td>
                        <td>{{ s.status ? 'ACTIVE' : 'INACTIVE' }}</td>
                        <td v-if="s.status">
                            <router-link v-if="this.roleId == 1" :to="{ path: '/detail/student/' + s.id }"
                                class="btn btn-primary me-3">Details</router-link>
                            <button v-if="this.roleId == 2" class="btn btn-primary" type="submit"
                                @click="openEditForm(s.id)" data-bs-toggle="modal" data-bs-target="#exampleModal"
                                style="margin-left: 20px;">Edit</button>
                            <button v-if="this.roleId == 1" class="btn btn-success" type="submit"
                                @click="openClassModal(s.id)" data-bs-toggle="modal" data-bs-target="#classModal"
                                style="margin-left: 20px;">Join class</button>
                            <button v-if="this.$store.state.userId != s.id && this.$store.state.authorities != 'ROLE_SUPERVISOR'" class="btn btn-warning" type="submit"
                                @click="deleteUser(s.id)" style="margin-left: 20px;">Delete</button>
                        </td>
                        <td v-else>
                            <button v-if="this.roleId == 1" class="btn btn-success" type="submit"
                                @click="exportData(s.id)" style="margin-left: 20px;">Export</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">User Information</h5>
                        <button id="closeModal" type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="form-group row">
                                <table class="table table-borderless">
                                    <tbody>
                                        <tr>
                                            <th scope="row"></th>
                                            <td>
                                                <input type="hidden" v-model="id">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"><label>Full name</label></th>
                                            <td>
                                                <input ref="name" class="form-control col-sm-8" placeholder="Full Name"
                                                    type="text" v-model="state.fullName" />
                                                <span v-if="v$.fullName.$error" style="color: red;">{{
                                                        v$.fullName.$errors[0].$message
                                                }}</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"><label>Email Address</label></th>
                                            <td>
                                                <input ref="email" class="form-control col-sm-8" placeholder="Email"
                                                    type="text" v-model="state.email" />
                                                <span v-if="v$.email.$error" style="color: red;">{{
                                                        v$.email.$errors[0].$message
                                                }}</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"> <label>Phone number</label></th>
                                            <td colspan="2">
                                                <input ref="phone" class="form-control col-sm-8"
                                                    placeholder="Phone Number" type="text" v-model="state.phone" />
                                                <span v-if="v$.phone.$error" style="color: red;">{{
                                                        v$.phone.$errors[0].$message
                                                }}</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"> <label>Username</label></th>
                                            <td colspan="2">
                                                <input ref="phone" class="form-control col-sm-8" placeholder="Username"
                                                    type="text" v-model="state.username" />
                                                <span v-if="v$.username.$error" style="color: red;">{{
                                                        v$.username.$errors[0].$message
                                                }}</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"> <label>Password</label></th>
                                            <td colspan="2">
                                                <input ref="phone" class="form-control col-sm-8" type="password"
                                                    v-model="state.password"
                                                    placeholder="Leave this field blank if not want to change password" />
                                                <span v-if="v$.password.$error" style="color: red;">{{
                                                        v$.password.$errors[0].$message
                                                }}</span>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>

                    </div>
                    <div class="modal-footer">
                        <input class="btn btn-primary " type="submit" @click="saveUser()" value="Save">
                        <input class="btn btn-danger " type="reset" value="Reset" @click="clearData()">
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

                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary">Join</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <pagination v-model="page" :records="listings.total" :per-page="listings.per_page" @paginate="paginate" />
</template>

<script>
import axios from 'axios'
import Pagination from 'v-pagination-3'

import { reactive, computed } from 'vue'
import useVuelidate from '@vuelidate/core'
import { required, email, minLength, maxLength, helpers } from '@vuelidate/validators'



const paperUrl = 'http://localhost:8080/api/v1/paper/'
const url = 'http://localhost:8080/api/v1/user/'


export default {
    setup() {
        const state = reactive({
            fullName: '',
            username: '',
            password: '',
            email: '',
            phone: '',
        })

        const onlyDigits = /^[a-zA-Z ]*$/;
        //  const phoneNumbers = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;

        const isNameValid = (value) => onlyDigits.test(value)
        // const isPhoneValid = (value) => phoneNumbers.test(value)

        const rules = computed(() => {
            return {
                fullName: {
                    required,
                    minLength: minLength(5),
                    maxLength: maxLength(40),
                    isNameValid: helpers.withMessage('Name must be only digits', isNameValid)
                },
                email: { required, email },
                phone: {
                    required,
                    minLength: minLength(10),
                    maxLength: maxLength(10)
                    // isPhoneValid: helpers.withMessage('Phone number not valid ! \n-start with 84|0[3|5|7|8|9]',isPhoneValid)
                },
                username: { required },
                password: { minLength: minLength(5) }
            }
        })

        const v$ = useVuelidate(rules, state)

        return {
            state,
            v$
        }
    },
    name: 'UserCpn',
    components: {
        Pagination: Pagination
    },
    data() {
        return {
            user: [],
            classes: [],
            papers: [],
            paper: '',
            paperId: 0,
            currentId: '',
            fileExcel: null,
            roles: '',
            roleId: 1,
            theory: '',
            practice: '',
            id: '',
            classId: '',
            className: '',
            key: '',
            noNum : '',
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
        this.getListUser()
        this.loadClasses()
        this.loadRoles()
    },
    methods: {
        async loadRoles() {
            const response = await axios.get('http://localhost:8080/api/v1/role/', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })

            const data = await response.data.body.list
            this.roles = data
        },
        async getListUser() {
            const response = await axios.get(url, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await response.data.body.pages
            this.noNum = data.size * data.number+1
            this.loadUser(data.content)
            this.logData(data)
        },
        loadClasses() {
            axios.get('http://localhost:8080/api/v1/class/data', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            }).then(response => (this.classes = response.data.body.list))
        },
        async paginate(e) {
            const response = await axios
                .get(url + '?skip=' + (e - 1) + '&key=' + this.key + '&role=' + this.roleId, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                })
            const data = await response.data.body.pages
            this.noNum = data.size * data.number+1
            this.loadUser(data.content)
            this.logData(data)    
        },
        logData(data) {
            this.page = data.pageable.pageNumber + 1
            this.listings.total = data.totalElements
            this.listings.per_page = data.pageable.pageSize
        },
        loadUser(data) {
            this.user = data

        },
        changeFile() {
            this.fileExcel = this.$refs.file.files[0]
        },
        saveMark() {
            axios.put(paperUrl + document.getElementById('paperId').value, { 'theory': this.theory, 'practice': this.practice }, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            }).then(response => (this.handleData(response.data)))
            this.openDetail(this.currentId);
        },
        async deleteUser(id) {
            if (confirm('Do you really want to delete this user?')) {
                const response = await axios.delete(url + id + '?roleId=' + this.roleId, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                })
                const data = response.data
                this.handleData(data)
            }
        },
        loadData(data) {
            this.id = data.id
            this.state.fullName = data.fullName
            this.state.email = data.email
            this.state.phone = data.phone
            this.state.username = data.username
            this.state.password = data.password
            this.classId = data.classId
            this.className = data.className
        },
        clearData() {
            this.id = ''
            this.state.fullName = ''
            this.state.email = ''
            this.state.phone = ''
            this.state.username = ''
            this.state.password = ''
            this.classId = ''
            this.className = ''
        },
        async openDetail(id) {
            this.currentId = id
            const fistRequest = await axios.get(url + id, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await fistRequest.data.body

            const secondRequest = await axios.get(url + id + '/paper', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const papers1 = await secondRequest.data.body.papers

            this.loadData(data)
            this.papers = papers1
        },
        async loadUserDetail(id) {
            const response = await axios.get(url + id, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await response.data.body

            this.loadData(data)

        },
        openClassModal(id) {
            this.currentId = id
            this.loadClasses()
        },
        exportData(id) {

            const header = {
                'Authorization': 'Bearer ' + this.$store.state.token
            }
            axios.get(url + id + '/excel', {
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
        saveUser() {
            this.v$.$validate();
            if (!this.v$.$error) {
                const id = this.id
                const data = {
                    'name': this.state.fullName,
                    'email': this.state.email,
                    'phone': this.state.phone,
                    'username': this.state.username,
                    'password': this.state.password,
                    'roleIds':[
                        this.roleId
                    ]
                }
                // console.log(data)
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
                    axios.put(url + id, data, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + this.$store.state.token
                        }
                    }).then(response => (this.handleData(response.data)))
                        .finally(
                            this.clearData(),
                            document.getElementById('closeModal').click()
                        )
                } else {
                    axios.post(url, data, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + this.$store.state.token
                        }
                    }).then(response => (this.handleData(response)))
                        .finally(
                            this.getListUser(),
                            document.getElementById('closeModal').click()
                        )
                }
            }

        },
        async importFile() {
            if (this.fileExcel != null) {
                const formData = new FormData();
                formData.append('file', this.fileExcel);
                const response = await axios.post(url + 'import/' + '?num=' + this.roleId, formData, {
                    headers: {
                        'Content-Type': 'application/json',
                        'content-type': 'multipart/form-data',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                })
                this.handleData(response.data)
            } else {
                alert("Please choose a file")
            }
        },
        async loadPaper(UserId, classId) {
            const response = await axios.get(url + UserId + '/paper/' + classId, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = response.data
            this.paper = data
            this.paperId = data.id
        },
        handleData(data) {
            console.log(data)
            if (data.message != undefined || data.message != null) {
                alert(data.message)
            }
            window.location.reload()
        },
        openEditForm(id) {
            this.loadUserDetail(id)

        },
        async search(key) {
            const response = await axios.get(url + '?key=' + key + '&role=' + this.roleId, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = response.data.body.pages.content
            this.user = data
            this.logData(response.data.body.pages)
        },
        async join() {
            axios.post(paperUrl, { 'studentId': this.currentId, 'classId': this.classId }, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            }).then(response => (this.handleData(response.data)))
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

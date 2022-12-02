<template>
    <div id="SubjectCpn">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <form class="d-flex">
                        <input class="form-control" type="search" v-model="key"
                            placeholder="Enter subject name to search" aria-label="Search">
                    </form>
                    <button class="btn btn-success" type="button" style="margin-left: 20px;float:right;" @click="search(key)">Search</button>
                </div>

                <button class="btn btn-primary" type="button" style="float:right;" @click="importFile()">Import</button>
                <input class="file" type="file" @change="changeFile()" ref="file"
                    style="margin-left: 20px;float:right; width: 300px;" />


                <button class="btn btn-success" type="button" data-bs-toggle="modal" data-bs-target="#exampleModal"
                    style="margin-left: 20px;float:right;">Add Subject</button>
            </div>
        </nav>

        <br>
        <div class="tabla-usuarios" style="width: 1000px;">
            <br>
            <label>
                <h2>List Subject</h2>
            </label>
            <table class="table table-hover table-striped table-bordered">
                <thead>
                    <th>NO</th>
                    <th>Name</th>
                    <th>Status</th>
                    <th>Action</th>
                </thead>
                <tbody>
                    <tr v-for="(s, index) in subjects" v-bind:key="s.id">
                        <td>{{ index + noNum }}</td>
                        <td>{{ s.subjectName }}</td>
                        <td>{{ s.status ? 'Normal' : 'Stopped' }}</td>
                        <td>
                            <div v-if="s.status">
                                <button class="btn btn-primary" type="submit" @click="openDetailForm(s.id)"
                                    data-bs-toggle="modal" data-bs-target="#detailModal"
                                    style="margin-left: 20px;">Detail</button>
                                <button class="btn btn-primary" type="submit" @click="openEditForm(s.id)"
                                    data-bs-toggle="modal" data-bs-target="#exampleModal"
                                    style="margin-left: 20px;">Edit</button>
                                <button class="btn btn-warning" type="submit" @click="deleteSubject(s.id)"
                                    style="margin-left: 20px;">Stop</button>
                            </div>
                            <div v-else>
                                <button class="btn btn-success" type="submit" @click="deleteSubject(s.id)"
                                    style="margin-left: 20px;">Continue</button>
                            </div>

                        </td>
                    </tr>
                </tbody>
            </table>
            <pagination v-model="page" :records="listings.total" :per-page="listings.per_page" @paginate="paginate" />
        </div>
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Subject Information</h5>
                        <button id="closeModal" type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
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
                                        <th scope="row"><label>Subject name</label></th>
                                        <td>
                                            <input ref="name" class="form-control col-sm-8" placeholder="Subject Name"
                                                type="text" v-model="state.name" />
                                            <span v-if="v$.name.$error"
                                                style="color: red;">{{ v$.name.$errors[0].$message }}</span>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input class="btn btn-primary" type="submit" @click="saveSubject()" value="Save">
                        <input class="btn btn-danger" type="reset" value="Reset" @click="clearData()">
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
                        <button id="closeModal" type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                    </div>
                </div>
                <div class="modal-body">
                    <div class="input-group flex-nowrap" style="margin-bottom: 20px;">
                        <span class="input-group-text" id="addon-wrapping">Id</span>
                        <input type="text" readonly class="form-control" :placeholder="id"
                            aria-describedby="addon-wrapping" disabled>
                    </div>
                    <div class="input-group flex-nowrap" style="margin-bottom: 20px;">
                        <span class="input-group-text" id="addon-wrapping2">Name</span>
                        <input type="text" readonly class="form-control" :placeholder="state.name"
                            aria-describedby="addon-wrapping2" disabled>
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
                                <td>{{ index + 1 }}</td>
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

</template>

<script>
import axios from 'axios'
import Pagination from 'v-pagination-3'

import { reactive, computed } from 'vue'
import useVuelidate from '@vuelidate/core'
import { required, minLength, maxLength } from '@vuelidate/validators'

const url = 'http://localhost:8080/api/v1/subject/'
export default {
    setup() {
        const state = reactive({
            name: '',
        })

        const rules = computed(() => {
            return {
                name: {
                    required,
                    minLength: minLength(1),
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
            key: '',
            fileExcel: '',
            roles: '',
            noNum : '',
            selectedId: '',
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
        async getSubjects() {
            const response = await axios.get(url, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await response.data.body.pages
            this.loadSubjects(data.content)
            this.logData(data)
            this.noNum = data.size * data.number + 1
        },
        loadSubjects(data) {
            this.subjects = data
        },
        changeFile() {
            this.fileExcel = this.$refs.file.files[0]
        },
        async importFile() {
            if (this.fileExcel != null && this.fileExcel != "") {
                const formData = new FormData();
                formData.append('file', this.fileExcel);
                const response = await axios.post(url + 'import', formData, {
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
        async search(key) {
            const response = await axios.get(url + '?key=' + key, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await response.data.body.pages.content
            this.subjects = data
            this.logData(data)
        },
        async openDetailForm(id) {
            const response = await axios.get(url + id + '/classes', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = response.data.body
            this.openEditForm(id)
            this.classes = data.list
        },
        logData(data) {
            this.page = data.pageable.pageNumber + 1
            this.listings.total = data.totalElements
            this.listings.per_page = data.pageable.pageSize
        },
        clearData(){
            this.id = '',
            this.state.name = ''
        },
        async paginate(e) {
            const response = await axios
                .get(url + '?skip=' + (e - 1), {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                })
                const data = await response.data.body.pages
            this.subjects = data.content
            this.logData(data)
            this.noNum = data.size * data.number + 1
        },
        handleData(data) {
            console.log(data)
            if (data.message != undefined || data.message != null) {
                alert(data.message)
            }
            window.location.reload()
        },
        saveSubject() {
            this.v$.$validate();
            if (!this.v$.$error) {
                if (this.id != null && this.id !== '') {
                    axios.put(url + this.id, { 'name': this.state.name }, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + this.$store.state.token
                        }
                    }).then(response => (alert(response.data.message)))
                        .finally(
                            window.location.reload()
                        )

                } else {
                    axios.post(url, { 'name': this.state.name }, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + this.$store.state.token
                        }
                    }).then(response => (alert(response.data.message)))
                        .finally(
                            window.location.reload()
                        )
                }
            }
        },
        async openEditForm(id) {
            const response = await axios.get(url + id, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await response.data.body
            this.loadSubjectDetail(data)
        },
        loadSubjectDetail(data) {
            this.id = data.id
            this.state.name = data.name
        },
        async deleteSubject(id) {
            const response = await axios.patch(url + id, null, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const msg = await response.data.message
            if (confirm(msg)) {
                window.location.reload()
            }
        },
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

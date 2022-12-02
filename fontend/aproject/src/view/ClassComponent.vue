<template>
    <div id="CategoryCpn">
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <div class="container-fluid">
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <form class="d-flex">
                        <input class="form-control" type="search" v-model="key" placeholder="Enter class name to search"
                            aria-label="Search">
                    </form>
                    <select id="filter" class="form-control" style="width: 300px;margin-left: 20px;"
                        v-model="selectedId" @change="filterClass()">
                        <option value="" disabled hidden>Select a subject</option>
                        <option v-for="s in subjects" v-bind:key="s.id" :value="s.id">{{ s.subjectName }}</option>
                    </select>
                    <button class="btn btn-success" type="button" style="margin-left: 20px;float:right;"
                        @click="search(key)">Search</button>
                </div>
                <div v-if="this.$store.state.authorities != 'ROLE_SUPERVISOR'">
                <button class="btn btn-primary" type="button" style="float:right;" @click="importFile()">Import</button>
                <input class="file" type="file" @change="changeFile()" ref="file"
                    style="margin-left: 20px;float:right; width: 300px;" />
                </div>

                <button class="btn btn-success" type="button" data-bs-toggle="modal" data-bs-target="#exampleModal1"
                    style="margin-left: 20px;float:right;">Add Class</button>
            </div>
        </nav>
        <br>


        <div class="tabla-usuarios" style="width: 1000px;">
            <label>
                <h2>List Class</h2>
            </label>
            <table class="table table-hover table-bordered">
                <thead>
                    <th>NO</th>
                    <th>Class Name</th>
                    <th>Time</th>
                    <th>Status</th>
                    <th>Action</th>
                </thead>
                <tbody v-if="classes.length > 0">
                    <tr v-for="(c, index) in classes" :key="c.id">
                        <td>{{ index + noNum }}</td>
                        <td>{{ c.className }}</td>
                        <td>{{ c.startDate + " --- " + c.endDate }}</td>
                        <td>{{ c.status }}</td>
                        <td v-if="c.status != 'ENDED'">
                            <button class="btn btn-primary" type="submit" @click="openDetail(c.id)"
                                data-bs-toggle="modal" data-bs-target="#detailModal"
                                style="margin-left: 20px;">Detail</button>
                            <button v-if="this.$store.state.authorities != 'ROLE_SUPERVISOR'" class="btn btn-primary"
                                type="submit" @click="openEditFormClass(c.id)" data-bs-toggle="modal"
                                data-bs-target="#exampleModal1" style="margin-left: 20px;">Edit</button>
                            <button v-if="(c.status == 'ONGOING' && this.$store.state.authorities != 'ROLE_SUPERVISOR')"
                                class="btn btn-danger" type="submit" @click="endClass(c.id)"
                                style="margin-left: 20px;">End</button>
                            <button v-if="c.status == 'CREATED' &&  this.$store.state.authorities != 'ROLE_SUPERVISOR'" class="btn btn-danger" type="submit"
                                @click="deleteClass(c.id)" style="margin-left: 20px;">Delete</button>
                        </td>
                        <td v-else>
                            <button class="btn btn-success" type="submit" @click="exportClass(c.id)"
                                style="margin-left: 20px;">Export</button>
                        </td>

                    </tr>
                </tbody>
                <tbody v-else>
                    <tr>
                        <td colspan="5">No class yet</td>
                    </tr>
                </tbody>
            </table>
            <br>
        </div>
        <div class="modal fade" id="exampleModal1" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Class Information</h5>
                        <button id="closeModal1" type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form>
                            <div class="form-group row">
                                <table class="table table-bordered">
                                    <tbody>
                                        <tr>
                                            <th scope="row"></th>
                                            <td>
                                                <input type="hidden" v-model="classId">
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"> <label>Class Name</label></th>
                                            <td>
                                                <input ref="className" class="form-control col-sm-8"
                                                    placeholder="Class Name" type="text" v-model="state.className" />
                                                <span v-if="v$.className.$error"
                                                    style="color: red;">{{ v$.className.$errors[0].$message }}</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"><label>Subject</label></th>
                                            <td colspan="2">
                                                <select class="form-control col-sm-8" v-model="state.subjectId"
                                                    :disabled="isEdit">
                                                    <option value="" disabled hidden>Select a subject</option>
                                                    <option v-for="s in subjects" v-bind:key="s.id" :value="s.id"
                                                        placeholder="Choose a subject">{{ s.subjectName }}</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"><label>Start date</label></th>
                                            <td colspan="2">
                                                <Datepicker v-model="startDate" utc />
                                            </td>
                                        </tr>
                                        <tr>
                                            <th scope="row"><label>End date</label></th>
                                            <td colspan="2">
                                                <Datepicker v-model="endDate" utc />
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <input class="btn btn-primary" type="submit" @click="saveClass()" value="Save">
                        <input class="btn btn-danger" type="reset" value="Reset" @click="clearData()">
                    </div>
                </div>
            </div>
        </div>

        <div class="container">
        </div>
        <!-- Modal -->
        <div class="modal fade" id="modalPaper" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header float-right">
                        <h5>Class : {{ currentClass }}</h5>
                        <div class="text-right">
                            <i data-dismiss="modal" aria-label="Close" class="fa fa-close"></i>
                        </div>
                    </div>
                    <div class="modal-body">
                        <div>
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Student name</th>
                                        <th scope="col">Mark</th>
                                        <th scope="col">Result</th>
                                    </tr>
                                </thead>
                                <tbody v-if="papers.length > 0">
                                    <tr v-for="(p, index) in papers" :key="p.id">
                                        <th scope="row">{{ index + noNum }}</th>
                                        <td scope="row">{{ p.studentName }}</td>
                                        <td scope="row">{{ p.mark }}</td>
                                        <td scope="row">{{ p.isPass ? "Pass" : "Fail" }}</td>
                                    </tr>
                                </tbody>
                                <tbody v-else>
                                    <tr>
                                        <th colspan="4">No student yet</th>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="detailModal" tabindex="-1" aria-labelledby="classModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header float-right">
                    <h5>Class Detail</h5>
                    <div class="text-right">
                        <button id="closeModal" type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close"></button>
                    </div>
                </div>
                <div class="modal-body">
                    <div class="input-group flex-nowrap" style="margin-bottom: 20px;">
                        <span class="input-group-text" id="addon-wrapping">Class Id</span>
                        <input type="text" readonly class="form-control" :placeholder="classId"
                            aria-describedby="addon-wrapping" disabled>
                    </div>
                    <div class="input-group flex-nowrap" style="margin-bottom: 20px;">
                        <span class="input-group-text" id="addon-wrapping2">Class name</span>
                        <input type="text" readonly class="form-control" :placeholder="state.className"
                            aria-describedby="addon-wrapping2" disabled>
                    </div>
                    <div class="input-group flex-nowrap" style="margin-bottom: 20px;">
                        <span class="input-group-text" id="addon-wrapping2">Start date</span>
                        <input type="text" readonly class="form-control" :placeholder="timemap"
                            aria-describedby="addon-wrapping2" disabled>
                    </div>
                    <br>
                    <table class="table table-bordered">
                        <thead>
                            <th>NO</th>
                            <th>Student</th>
                            <th>Theory</th>
                            <th>Preactice</th>
                            <th>Status</th>
                        </thead>
                        <tbody>
                            <tr v-for="(p, index) in papers" :key="p.id">
                                <td>{{ index + 1 }}</td>
                                <td>{{ p.studentName }}</td>
                                <td>{{ p.theory == null ? 'Not mark yet' : p.theory }}</td>
                                <td>{{ p.practice == null ? 'Not mark yet' : p.practice }}</td>
                                <td>{{ p.isPass == null ? 'Not complete' : p.isPass ? 'Passed' : 'Failed' }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-success" type="submit" @click="exportData()"
                        style="margin-left: 20px;">Export</button>
                </div>
            </div>
        </div>
    </div>

    <pagination v-model="page" :records="listings.total" :per-page="listings.per_page" @paginate="paginate" />
</template>

<script>
import axios from 'axios'
import Pagination from 'v-pagination-3'
import Datepicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'
import { reactive, computed } from 'vue'
import useVuelidate from '@vuelidate/core'
import { required, minLength, maxLength } from '@vuelidate/validators'
import moment from 'moment'

const urlClass = 'http://localhost:8080/api/v1/class/'
export default {
    setup() {
        const state = reactive({
            className: '',
            subjectId: ''
        })

        const rules = computed(() => {
            return {
                className: {
                    required,
                    minLength: minLength(10),
                    maxLength: maxLength(100),
                },
                subjectId: {
                    required
                }
            }
        })

        const v$ = useVuelidate(rules, state)

        return {
            state,
            v$
        }
    },
    name: 'CategoryCpn',
    components: {
        Pagination: Pagination,
        Datepicker,
    },
    data() {
        return {
            timemap: '',
            now : new Date(),
            startDate: new Date(),
            endDate: new Date(),
            classes: [],
            subjects: [],
            papers: [],
            classId: '',
            isEdit: false,
            isSelected: false,
            selectedId: '',
            currentId: '',
            fileExcel: '',
            currentClass: '',
            noNum: '',
            key: '',
            page: 1,
            listings: {
                total: 0,
                per_page: 0
            }
        }
    },
    computed() {

    },
    created() {
        this.loadClasses()
        this.loadSubject()

    },
    methods: {
        changeFile() {
            this.fileExcel = this.$refs.file.files[0]
        },
        async importFile() {
            if (this.fileExcel != null && this.fileExcel != "") {
                const formData = new FormData();
                formData.append('file', this.fileExcel);
                const response = await axios.post(urlClass + 'import', formData, {
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
        async deleteClass(id) {
            if (confirm('Do you really want to delete this class?')) {
                const response = await axios.delete(urlClass + id, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                })
                this.loadClasses()
                alert(response.data.message)
            }
        },
        exportData() {
            const header = {
                'Authorization': 'Bearer ' + this.$store.state.token
            }
            axios.get(urlClass + this.currentId + '/excel', {
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
        async openDetail(id) {
            this.currentId = id
            const fistRequest = await axios.get(urlClass + id, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await fistRequest.data.body
            const secondRequest = await axios.get(urlClass + id + '/paper', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data1 = await secondRequest.data.body.papers
            this.loadDataClass(data)

            this.papers = data1
        },
        async endClass(id) {
            if (confirm('Do you really want to end this class?')) {
                const response = await axios.patch(urlClass + id, null, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                })
                this.loadClasses()
                console.log(response.oiro)
            }
        },
        async paginate(e) {
            const response = await axios
                .get(urlClass + '?skip=' + (e - 1), {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                })
            const data = await response.data.body.pages
            this.loadClassesData(data.content)
            this.logData(data)
            this.noNum = data.size * data.number + 1
        },
        loadDataClass(data) {
            this.classId = data.id
            this.state.code = data.code
            this.state.className = data.className
            this.state.subjectId = data.subjectId
            this.startDate = data.startDate
            this.endDate = data.endDate
            this.timemap = moment(data.startDate).format('DD-MM-YYYY') + " to " + moment(data.end).format('DD/MM/YYYY')
        },
        logData(data) {
            this.page = data.pageable.pageNumber + 1
            this.listings.total = data.totalElements
            this.listings.per_page = data.pageable.pageSize
        },
        exportClass(id) {
            const header = {
                'Authorization': 'Bearer ' + this.$store.state.token
            }
            axios.get(urlClass + id + '/excel', {
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
        clearData() {
            this.classId = ''
            this.state.code = ''
            this.state.className = ''
            this.isEdit = false
        },
        async loadClassDetail(id) {
            const response = await axios.get(urlClass + id, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await response.data.body
            this.loadDataClass(data)
        },
        handleData(data) {
            console.log(data)
            if (data.message != undefined || data.message != null) {
                alert(data.message)
            }
            this.loadClasses()
            this.clearData()
        },
        async getCurrentClass(id) {
            const fistRequest = await axios.get(urlClass + id, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            this.currentClass = fistRequest.data.className
            const secondRequest = await axios.get(urlClass + id + '/paper', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            this.papers = secondRequest.data.body
        },
        openEditFormClass(id) {
            this.loadClassDetail(id)
            this.isEdit = true
        },
        async filterClass() {
            this.isSelected = true;
            // const response = await axios.get(urlClass+'?subjectId='+this.selectedId,{
            //             headers: {
            //                 'Content-Type': 'application/json',
            //                 'Authorization':'Bearer ' + this.$store.state.token
            //             }})
            // const data = await response.data.body.pages
            // this.loadClassesData(data.content)
            // this.logData(data)
        },
        async loadSubject() {
            const response = await axios.get('http://localhost:8080/api/v1/subject/data', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = response.data.body.list
            this.subjects = data
        },
        saveClass(e) {
            this.v$.$validate();
            if (!this.v$.$error) {
                const id = this.classId
                console.log(this.now)
                if (this.startDate <= this.now ) {
                    alert("Time range invalid!")
                    e.preventDefault();
                }
                if (this.endDate <= this.now ) {
                    alert("Time range invalid!")
                    e.preventDefault();
                }
                if (this.startDate < this.endDate) {
                    alert("Time range invalid!")
                    e.preventDefault();
                }
                if (id != null && id !== '') {
                    axios.put(urlClass + id, { 'className': this.state.className, 'startDate': this.startDate, 'endDate': this.endDate }, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + this.$store.state.token
                        }
                    }).then(response => (this.handleData(response.data)))
                        .finally(
                            document.getElementById('closeModal1').click(),
                            window.location.reload()
                        )
                } else {
                    axios.post(urlClass, { 'className': this.state.className, 'startDate': this.startDate, 'endDate': this.endDate, 'subjectId': this.state.subjectId }, {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + this.$store.state.token
                        }
                    }).then(response => (this.handleData(response.data)))
                        .finally(
                            document.getElementById('closeModal1').click(),
                            window.location.reload()
                        )
                }
            }
        },
        async loadClasses() {
            const response = await axios.get(urlClass, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await response.data.body.pages
            this.loadClassesData(data.content)
            this.logData(data)
            this.noNum = data.size * data.number + 1
        },
        loadClassesData(data) {
            this.classes = data
        },
        async search(key) {
            let baseUrl = urlClass + '?key=' + key;
            const url = (this.isSelected == false) ? baseUrl : (baseUrl + '&subjectId=' + this.selectedId)
            const response = await axios.get(url, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data = await response.data.body.pages.content
            this.classes = data
            this.logData(response.data.body.pages)
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

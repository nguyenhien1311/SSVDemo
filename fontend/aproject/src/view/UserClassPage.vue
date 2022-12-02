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
            </div>
        </nav>
        <br>


        <div class="tabla-usuarios" style="width: 1000px;">
            <label>
                <h2>List Class</h2>
            </label>
            <table class="table table-hover table-bordered table-active">
                <thead>
                    <th>NO</th>
                    <th>Class Name</th>
                    <th>Time</th>
                    <th>Action</th>
                </thead>
                <tbody v-if="classes.length > 0">
                    <tr v-for="(c, index) in classes" :key="c.id">
                        <td>{{ index + noNum }}</td>
                        <td>{{ c.className }}</td>
                        <td>{{ c.startDate + " --- " + c.endDate }}</td>
                        <td>
                            <button class="btn btn-success" type="submit" @click="joinClass(c.id)"
                                style="margin-left: 20px;">Join</button>
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
    </div>
    <pagination v-model="page" :records="listings.total" :per-page="listings.per_page" @paginate="paginate" />
</template>

<script>
import axios from 'axios'
import Pagination from 'v-pagination-3'
import moment from 'moment'

const urlClass = 'http://localhost:8080/api/v1/class/'
export default {

    name: 'CategoryCpn',
    components: {
        Pagination: Pagination,

    },
    data() {
        return {
            timemap: '',
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
    created() {
        this.loadClasses()
        this.loadSubject()

    },
    methods: {

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
        async paginate(e) {
            const response = await axios
                .get(urlClass + 'data/student' + '?skip=' + (e - 1), {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                })
               const data =  response.data.body.pages
            this.classes = data.content
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
        handleData(data) {
            console.log(data)

            if (data.message != undefined || data.message != null) {
                alert(data.message)
            }

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

        async loadClasses() {
            const response = await axios.get(urlClass + 'data/student', {
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
        joinClass(id) {
            axios.post('http://localhost:8080/api/v1/paper/', { 'studentId': this.$store.state.userId, 'classId': id }, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            }).then(response => (this.handleData(response.data)))
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

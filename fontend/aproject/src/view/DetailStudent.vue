<template>
    <div style="width: 1000px;">
        <br />
        <h3>Account details</h3>
        <table class="table table-bordered tabla-usuarios table-hover">
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
                        <input ref="name" class="form-control col-sm-8" placeholder="Full Name" type="text"
                            v-model="state.fullName" />
                        <span v-if="v$.fullName.$error" style="color: red;">{{ v$.fullName.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"><label>Email Address</label></th>
                    <td>
                        <input ref="email" class="form-control col-sm-8" placeholder="Email" type="text"
                            v-model="state.email" />
                        <span v-if="v$.email.$error" style="color: red;">{{ v$.email.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"> <label>Phone number</label></th>
                    <td colspan="2">
                        <input ref="phone" class="form-control col-sm-8" placeholder="Phone Number" type="text"
                            v-model="state.phone" />
                        <span v-if="v$.phone.$error" style="color: red;">{{ v$.phone.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"> <label>Username</label></th>
                    <td colspan="2">
                        <input ref="phone" class="form-control col-sm-8" placeholder="Username" type="text"
                            v-model="state.username" />
                        <span v-if="v$.username.$error" style="color: red;">{{ v$.username.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"> <label>Password</label></th>
                    <td colspan="2">
                        <input ref="phone" class="form-control col-sm-8"
                            placeholder="Leave this field blank if not want to change password" type="text"
                            v-model="state.password" />
                        <span v-if="v$.password.$error" style="color: red;">{{ v$.password.$errors[0].$message }}</span>
                    </td>
                </tr>
                <tr>
                    <th scope="row"></th>
                    <td colspan="2">
                        <input class="btn btn-primary " type="submit" @click="saveUser()" value="Save" />
                        <button class="btn btn-success" type="submit" @click="exportData()"
                            style="margin-left: 20px;">Export</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <br>
        <table class="table table-bordered">
            <thead>
                <th>NO</th>
                <th>Class Name</th>
                <th>Mark Theory</th>
                <th>Mark Practice</th>
                <th>Status</th>
                <th>Action</th>
            </thead>
            <tbody>
                <tr v-for="(p, index) in papers" :key="p.id">
                    <td>{{ index + 1 }}</td>
                    <td>{{ p.className }}</td>
                    <td>{{ p.theory == null ? '0' : p.theory }}</td>
                    <td>{{ p.practice == null ? '0' : p.practice }}</td>
                    <td>{{ p.isPass == null ? 'Not complete' : p.isPass ? 'Passed' : 'Failed' }}</td>
                    <td v-if="p.isPass == null">
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                            data-bs-target="#exampleModal" @click="openMarkForm(p.id)">Mark</button>
                    </td>
                </tr>
            </tbody>
        </table>





    </div>

    <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Mark form</h5>
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
                                        <input id="pid" type="hidden" v-model="pid">
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row"><label for="theory">Theory mark</label></th>
                                    <td>
                                        <input type="number" class="form-control mb-2 mr-sm-2" id="theory"
                                            v-model="state.theory">
                                        <span v-if="v$.theory.$error" style="color: red;">{{
                                                v$.theory.$errors[0].$message
                                        }}</span>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row"> <label for="theory">Practice mark</label></th>
                                    <td>
                                        <input type="number" class="form-control mb-2 mr-sm-2" id="practice"
                                            v-model="state.practice">
                                        <span v-if="v$.practice.$error" style="color: red;">{{
                                                v$.practice.$errors[0].$message
                                        }}</span>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="row"></th>
                                    <td>
                                        <button type="submit" @click="saveMark()"
                                            class="btn btn-primary mb-2">Submit</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { reactive, computed } from 'vue'
import useVuelidate from '@vuelidate/core'
import { required, email, minLength, maxLength, helpers, minValue, maxValue } from '@vuelidate/validators'
import axios from 'axios'

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
            theory: '',
            practice: '',
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
                theory: {  minValue: minValue(0), maxValue: maxValue(10) },
                practice: { minValue: minValue(0), maxValue: maxValue(10) },
                password: { minLength: minLength(5) }
            }
        })

        const v$ = useVuelidate(rules, state)

        return {
            state,
            v$
        }
    },
    data() {
        return {
            id: '',
            pid: '',
            papers: []
        }
    },
    created() {
        this.openDetail()
    },
    methods: {
        async openDetail() {
            this.id = this.$route.params.id
            const fistRequest = await axios.get(url + this.id, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const data =  fistRequest.data.body
           
            const secondRequest = await axios.get(url + this.id + '/paper', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.$store.state.token
                }
            })
            const papers1 =  secondRequest.data.body.papers

            this.loadData(data)
            this.papers = papers1
        },
        loadData(data) {
            this.state.fullName = data.fullName
            this.state.email = data.email
            this.state.phone = data.phone
            this.state.username = data.username
        },
        saveUser() {
            this.v$.$validate();
            if (!this.v$.$error) {
                const data = {
                    'name': this.state.fullName,
                    'email': this.state.email,
                    'phone': this.state.phone,
                    'username': this.state.username,
                    'password': this.state.password
                }
                axios.put('http://localhost:8080/api/v1/user/' + this.id, data, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                }).then(response => (this.handleData(response.data)))
            }
        },
        openMarkForm(id) {
            this.pid = id;
        },
        clearData() {
            this.state.fullName = ''
            this.state.email = ''
            this.state.phone = ''
            this.state.username = ''
            this.state.password = ''
        },
        saveMark() {
            this.v$.$validate();
            if (!this.v$.$error) {
                axios.put(paperUrl + this.pid, { 'theory': this.state.theory, 'practice': this.state.practice }, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + this.$store.state.token
                    }
                }).then(response => (this.handleData(response.data)))
                this.openDetail();
            }
        },
        exportData() {
            
            const header = {
                            'Authorization':'Bearer ' + this.$store.state.token
                        }
            axios.get(url+ this.id+'/excel',{
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
        handleData(data) {
            console.log(data)
            if (data.message != undefined || data.message != null) {
                alert(data.message)
            }

            window.location.reload()
        },
    }
}
</script>

<style>

</style>
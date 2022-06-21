
Vue.createApp({
    data() {
        return {
            loans:[],
            accountsDestiny:[],
            datosJson: [],
            selectedLoanId: "",
            selectedAmount: "",
            selectedPayments: "",
            numberAccountDestiny: "",
            checkCondition: false,
            interest : "",

        }
    },

    created() {

        axios.get(`http://localhost:8080/api/clients/current`)
            .then(datos => {
                this.datosJson = datos.data
                this.accountsDestiny = datos.data.accounts
                document.querySelector("#loader").classList.toggle("loader2")
            }
            ),
        axios.get(`/api/loans`)
        .then(datos =>{
            this.loans = datos.data
            console.log(this.loans);
        } 
        )

    },

    methods: {

        createLoan() {
            

            console.log(this.checkCondition);
            
            Swal.fire({
                title: "Confirmation",
                text: "Are you sure you want to apply for the loan?",
                icon: "warning",
                showCancelButton: true,
                cancelButtonText: 'No, cancel!',
                confirmButtonText: 'Yes, Request!',
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post('/api/loans',{ loanId: this.selectedLoanId, amount: this.selectedAmount, payments: this.selectedPayments, numberAccountDestiny: this.numberAccountDestiny})  
                        .then(response => {
                            Swal.fire({
                                title: "Approved!",
                                text: "The loan was approved.",
                                icon: "success",
                                button: "Ok"
                            })
                                .then(response => window.location.reload())
                        })
                        .catch(error => {
                            Swal.fire({
                                text: error.response.data,
                                icon: "error"
                            })
                        })
                }
            })



        },

        logout() {
            axios.post('/api/logout')
                .then(response => window.location.href = "http://localhost:8080/web/index.html"
                )

        },
        filterLoan() {
            let aux = [...this.loans]
            aux= aux.filter( (loan)=> this.selectedLoanId == loan.id);  
            return aux[0].payments
        },

        buscarInteres(){
            let aux = [...this.loans]
            aux= aux.filter( (loan)=> this.selectedLoanId == loan.id);       
            this.interest= aux[0].interest
        }


    }


}).mount('#app')
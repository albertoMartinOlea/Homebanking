Vue.createApp({
  data() {
    return {
      datosJson: [],
      arrayAccounts: [],
      arrayLoans: [],
      cardsJson:[],
      typeAccount: ""

    }
  },

  created() {
    axios.get(`/api/clients/current`)
      .then(datos => {

        // console.log(datos)
        this.datosJson = datos.data
        this.arrayAccounts = datos.data.accounts
        this.arrayLoans = datos.data.clientLoans
        this.cardsJson = datos.data.cards



      }
      )

      

  },

  methods: {

    logout() {
      axios.post('/api/logout')
        .then(response => window.location.href = "/web/index.html"
        )

    },

    createAccount() {


      const { value: fruit } = Swal.fire({
        title: 'Select field validation',
        input: 'select',
        inputOptions: {
          'Select account': {
            CURRENT: 'Current',
            SAVING: 'Saving',
          },

        },
        inputPlaceholder: 'Select account',
        showCancelButton: true,
        inputValidator: (value) => {
          return new Promise((resolve) => {
            if (value === 'CURRENT') {
              this.typeAccount = "CURRENT"
              console.log(this.typeAccount);
              resolve()
            }
            if (value === 'SAVING') {
              this.typeAccount = "SAVING"
              resolve()
            } else {
              resolve("Choose the type of account to create")
            }
          })
        }
      })
        .then(result => {
          if (result.isConfirmed) {
            axios.post(`/api/clients/current/accounts`, `type=${this.typeAccount}`)
              .then(response => {
                Swal.fire({
                  title: "Confirmed!",
                  text: "The account was created.",
                  icon: "success",
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

    deleteAccount(id) {
      Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
      }).then((result) => {
        if (result.isConfirmed) {
          axios.patch(`/api/clients/current/accounts/${id}`)
            .then(response => {
              Swal.fire(
                'Deleted!',
                'Your account has been deleted.',
                'success'
              )
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

    }

  }


}).mount('#app')

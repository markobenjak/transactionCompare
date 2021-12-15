<template>
  <div class="hello">
    <div class="inputFiles">
      <b-form-group label="Select File 1:" label-cols-sm="2" label-size="sm">
        <b-form-file 
          v-model="file1"
          :state="Boolean(file1)"
          placeholder="Choose a file or drop it here..."
          id="file1" 
          size="sm"
          accept=".csv"
          > 
        </b-form-file>
        <div class="validationError" v-if="!$v.file1.required"></div>
      </b-form-group>
      <b-form-group label="Select File 2:" label-cols-sm="2" label-size="sm">
        <b-form-file 
          v-model="file2"
          :state="Boolean(file2)"
          placeholder="Choose a file or drop it here..." 
          id="file2"
          size="sm"
          accept=".csv"
          >
        </b-form-file>
        <div class="validationError" v-if="!$v.file1.required"></div>
      </b-form-group>
        <b-button block variant="primary" @click="sendFiles" :disabled="checkCompareValidations()">Compare</b-button>
    </div>
    <div class="compareFiles" v-show="!showCompareResults">
      <b-card-group deck>
        <b-card
          :header=file1.name
        >
          <b-card-text>Total Records: {{totalRecordsFile1}}</b-card-text>
          <b-card-text>Matching Records: {{matchingRecordsFile1}}  </b-card-text>
          <b-card-text>Unmatched Records: {{totalUnmatchedRecordsFile1}}</b-card-text>
        </b-card>

        <b-card :header=file2.name>
          <b-card-text>Total Records: {{totalRecordsFile2}} </b-card-text>
          <b-card-text>Matching Records: {{matchingRecordsFile2}} </b-card-text>
          <b-card-text>Unmatched Records: {{totalUnmatchedRecordsFile2}} </b-card-text>
        </b-card>
      </b-card-group>
      <b-button block variant="primary" class="unmatchedReportButton" @click="showUnmatchedReportTables()">Unmatched Report</b-button>
    </div>
    <div class="unmatchedReportDiv" v-show="!showUnamtchedReport">
      <b-table
        caption-top
        sticky-header
        no-border-collapse
        small
        head-variant="light"
        responsive
        :items="unmatchedRecordsFile1"
        :fields="fieldsUnmatchedReport"
        ref="unmatchedReportTableFile1"
        id="unmatchedReportTableFile1"
        class="unmatchedReportTableFile1Class"
      >
        <template #table-caption>{{ file1.name }}</template>
      </b-table>
      <b-table
        caption-top
        sticky-header
        no-border-collapse
        small
        head-variant="light"
        responsive
        :items="unmatchedRecordsFile2"
        :fields="fieldsUnmatchedReport"
        ref="unmatchedReportTableFile2"
        id="unmatchedReportTableFile2"
        class="unmatchedReportTableFile2Class"
      >
        <template #table-caption>{{ file2.name }}</template>
      </b-table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { required } from 'vuelidate/lib/validators'
export default {
  name: 'MainPage',
  props: {
    msg: String
  },
  data : function() {
    return {
        file1: '',
        file2: '',
        showUnamtchedReport: true,
        showCompareResults: true,
        totalRecordsFile1: '',
        matchingRecordsFile1: '',
        totalUnmatchedRecordsFile1: '',
        totalRecordsFile2: '',
        matchingRecordsFile2: '',
        totalUnmatchedRecordsFile2: '',
        unmatchedRecordsFile1: [],
        unamtchedRecordsFile2: [],
        fieldsUnmatchedReport: [
                // {key: 'profileName', label: 'Profile Name' },
                {key: 'transactionDate', label: 'Tran. Date' },
                {key: 'walletReference', label: 'Wallet' },
                {key: 'transactionAmount', label: 'Tran. Amount' },
                // {key: 'transactionNarrative', label: 'Tran. Narrative' },
                // {key: 'transactionDescription', label: 'Tran. Description' },
                // {key: 'transactionID', label: 'Tran. ID' },
                // {key: 'transactionType', label: 'Tran. Type' },
            ],
    } 
  },
  validations: {
    file1: {
        required
    },
    file2: {
        required
    },
  },
  methods: {
    showUnmatchedReportTables(){
      this.showUnamtchedReport = false;
    },

    makeToast(title, variant, content, delay) {
      this.$bvToast.toast(content, {
          title: title,
          toaster: 'b-toaster-top-center',
          variant: variant,
          solid: true,
          "auto-hide-delay": delay
      })
    },

    checkCompareValidations (){
      if(this.$v.file1.$invalid || this.$v.file2.$invalid)
        return true;
      else
        return false;
    },

    sendFiles(){
        const headers = {
          'Content-Type': '"multipart/form-data"'       
        }
        let formData1 = new FormData();
        formData1.append('csvFile', this.file1);
        formData1.append('csvFile2', this.file2);
        console.log(formData1)
        let URL = 'api/uploadFiles';
        let promise = axios.post(URL, formData1, {
          headers: headers
          })
        return promise.then((response) => {
          console.log(response);
            if(response.data.responseMessage == 'SUCCESS'){
              alert('Password Changed Successfully');
              this.totalRecordsFile1 = response.data.totalNumberFile1;
              this.totalUnmatchedRecordsFile1 = response.data.unmatchedRecordsFile1;
              this.matchingRecordsFile1 = this.totalRecordsFile1 - this.totalUnmatchedRecordsFile1;
              this.unmatchedRecordsFile1 = response.data.clientProfileFile1;
              this.totalRecordsFile2 = response.data.totalNumberFile2;
              this.totalUnmatchedRecordsFile2 = response.data.unmatchedRecordsFile1;
              this.matchingRecordsFile2 = this.totalRecordsFile2 - this.totalUnmatchedRecordsFile2;
              this.unmatchedRecordsFile2 = response.data.clientProfileFile2;
              this.showCompareResults = false;
              this.makeToast('File Comparison Finished', 'primary', response.data.responseMessage, 10000);
            }

        },
        ).catch(err => {
            //alert(err.response.data.error);
            console.log(err);
        })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .inputFiles {
    width: 50%;
    margin : 0 auto;
  }
  .compareFiles {
    width: 50%;
    margin : 0 auto;
    margin-top:1%;
  }
  .unmatchedReportButton {
    margin-top: 1%;
  }
  .unmatchedReportDiv {
    margin-top: 1%;
  }
  .unmatchedReportTableFile1Class{
    float: left;
    width: 48%;
    margin-left:2%;
  }
  .unmatchedReportTableFile2Class{
    float: left;
    width: 48%;
    margin-left:2%;
  }
</style>

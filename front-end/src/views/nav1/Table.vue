<template>
	<section>
		<!--工具条-->
		<el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
			<el-form :inline="true" :model="filters">
				<el-form-item>
					<el-input v-model="filters.serviceID" placeholder="serviceID"></el-input>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" v-on:click="getServices">search</el-button>
				</el-form-item>
				<el-form-item>
					<el-button type="primary" @click="handleAdd">add</el-button>
				</el-form-item>
			</el-form>
		</el-col>

		<!--列表-->
		<el-table :data="servs" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%;">
			<el-table-column type="selection" width="55">
			</el-table-column>
			<el-table-column type="index" width="60">
			</el-table-column>
			<el-table-column prop="server_id" label="serviceID" width="240" sortable>
			</el-table-column>
			<el-table-column prop="name" label="name" width="100" sortable>
			</el-table-column>
			<el-table-column prop="description" label="description" min-width="160" sortable>
			</el-table-column>
			<el-table-column label="action" width="160">
				<template scope="scope">
					<el-button size="small" @click="showConfig(scope.$index, scope.row)">view</el-button>
					<el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">delete</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!--工具条-->
		<el-col :span="24" class="toolbar">
			<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="20" :total="total" style="float:right;">
			</el-pagination>
		</el-col>

		<!--config列表界面-->
		<el-dialog title="Config List" v-model="listFormVisible" :close-on-click-modal="false">
			<el-table :data="configList" @cell-click="getConfig" highlight-current-row
					  style="width: 100%; text-align: center;"
					  >
				<el-table-column prop="config_id" label="config" min-width="90" align="center" >
				</el-table-column>
				<el-table-column>
					<el-button type="danger"  size="small" @click="deleteConfig">delete</el-button>
				</el-table-column>
			</el-table>
			<div slot="footer" class="dialog -footer">
				<el-button @click.native="listFormVisible = false">back</el-button>
				<el-button type="primary" @click.native="showaddConfig">add</el-button>
			</div>

		</el-dialog>

		<!--config编辑界面-->
		<el-dialog title="edit config" v-model="editFormVisible" :close-on-click-modal="false">
			<el-form :model="objConfig" label-width="80px"  ref="editForm">
				<el-col :span="24">
					<el-form-item label="configID">
						<el-input v-model="objConfig.config_id" auto-complete="off"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="modifyDate">
						<el-date-picker type="date" placeholder="select" v-model="objConfig.modifiedDate"></el-date-picker>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="key">
						<el-input v-model="objConfig.key" ></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="value">
						<el-input v-model="objConfig.value" :min="0" :max="200"></el-input>
					</el-form-item>
				</el-col>
				<el-form-item label="description">
					<el-input type="textarea" v-model="objConfig.description" ></el-input>
				</el-form-item>
				<el-col :span="12">
					<el-form-item label="attr1">
						<el-input v-model="objConfig.attr1" :min="0" :max="200"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="attr2">
						<el-input v-model="objConfig.attr2" :min="0" :max="200"></el-input>
					</el-form-item>
				</el-col>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">back</el-button>
				<el-button type="primary" @click.native="configSubmit" :loading="editLoading">commit</el-button>
			</div>
		</el-dialog>

		<!--service新增界面-->
		<el-dialog title="add service" v-model="addFormVisible" :close-on-click-modal="false">
			<el-form :model="addForm" label-width="80px"  ref="addForm">
				<el-form-item label="name">
					<el-input v-model="addForm.name" :min="0" :max="200"></el-input>
				</el-form-item>
				<el-form-item label="discription">
					<el-input type="textarea" v-model="addForm.addr"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="addFormVisible = false">back</el-button>
				<el-button type="primary" @click.native="addSubmit" :loading="addLoading">commit</el-button>
			</div>
		</el-dialog>

		<!--config新增界面-->
		<el-dialog title="add config" v-model="addConfigVisible" :close-on-click-modal="false">
			<el-form :model="addConfig" label-width="80px"  ref="addConfig">
				<el-form-item label="key">
					<el-input v-model="addConfig.key" :min="0" :max="200"></el-input>
				</el-form-item>
				<el-form-item label="value">
					<el-input v-model="addConfig.value" :min="0" :max="200"></el-input>
				</el-form-item>
				<el-form-item label="description">
					<el-input type="textarea" v-model="addConfig.description"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="addConfigVisible = false">back</el-button>
				<el-button type="primary" @click.native="addConfigCommit" >commit</el-button>
			</div>
		</el-dialog>

	</section>
</template>

<script>
    import $ from 'jquery'
	import util from '../../common/js/util'
	//import NProgress from 'nprogress'
	import { getServicesListPage} from '../../api/api';

	export default {
		data() {
			return {
				filters: {
                    serviceID: ''
				},
				servs: [],
				total: 0,
				page: 1,
				listLoading: false,
				listFormVisible: false,
				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				configList:[],
                objConfig:[],
				addFormVisible: false,//新增界面是否显示
				addConfigVisible:false,//新增config界面是否显示
				addLoading: false,
				//新增界面数据
				addForm: {
					name: '',
					addr: ''
				},
				serviceID:'',
                addConfig:{
				    key:'',
					value:'',
					description:''
				}

			}
		},
		methods: {
			handleCurrentChange(val) {
				this.page = val;
				this.getServices();
			},
			//获取服务列表 mock版本
			getServicesMock() {
				let para = {
					page: this.page,
                    serviceID: this.filters.serviceID
				};
				this.listLoading = true;
				//NProgress.start();
				getServicesListPage(para).then((res) => {
					this.total = res.data.total;
					this.servs = res.data.servs;
					this.listLoading = false;
					//NProgress.done();
				});
			},
			//获取服务列表 访问接口版本
			getServices() {
                this.listLoading = true;
                var self = this;
                $.ajax({
                    url: 'http://localhost:3100/getServices/2855815742122075602',
                    type: "GET",
                    dataType: "json",
                    contentType: 'application/json',
                    success: function(data) {
                        self.servs = data;
                        self.total = data.length;
                        self.listLoading = false;
                    }
                });
			},
			//删除service
			handleDel: function (index, row) {
				this.$confirm('confirm delete?', 'instruction', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					console.log(this.serviceID);
					this.getServices();

				}).catch(() => {

				});
			},
            deleteConfig: function(){
			  	console.log(this.serviceID);
			  	console.log(this.objConfig.config_id);
			},
			//显示configList
			showConfig: function(index, row){
			    this.serviceID = Object.assign({}, row).server_id;
			    this.listFormVisible = true;
			    this.configList = Object.assign({}, row).config;
			},
			//显示编辑config界面
			getConfig: function (cell) {
			    this.objConfig = cell;
                this.editFormVisible = true;
			},
            //显示新增服务界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.addForm = {
                    name: '',
                    addr: ''
                };
            },
			//显示新增config界面
            showaddConfig: function() {
			    this.addConfigVisible = true;

			},
			//修改config
            configSubmit: function() {
				var config = {};
				var objConfig = this.objConfig;
				Object.keys(objConfig).forEach(function(key){
					config[key] = objConfig[key];
				});
				var url = "http://127.0.0.1:3100/updateConfig?serviceId=" + this.serviceID.toString()
                $.ajax({
                    url: url,
                    data: JSON.stringify(config),
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json',
                });
                alert("commit success");
                this.editFormVisible = false;
                this.listFormVisible = false;
                this.getServices();

			},
			//新增config
            addConfigCommit: function(){
				var configData = this.addConfig;
				var url = "http://127.0.0.1:3100/addConfig?serviceId=" + this.serviceID.toString();
				var self = this;
                $.ajax({
                    data: JSON.stringify({
                        key: configData.key,
                        value: configData.value,
                        description : configData.description
                    }),
                    url: url,
                    type: "POST",
                    dataType: "json",
					contentType: 'application/json'
                })
                alert("commit success");
                this.addConfigVisible = false;
                this.listFormVisible = false;
                this.getServices();
			},
			//新增service
			addSubmit: function () {
			    var serviceData = this.addForm;
                this.addLoading = true;
                $.ajax({
                    data:JSON.stringify({
                        name: serviceData.name,
                        description: serviceData.addr,
                        token:'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6ImFkbWluMSIsImV4cCI6MTU2MjY4MjYzNiwidXNlcm5hbWUiOiJhZG1pbjEifQ.St0aKLu0f_NjdlkQl041jrUdh89mWAEMu0lY8ZoxKnk'
                    }),
                    url:"http://localhost:3100/createService?userId=2855815742122075602",
                    type: "POST",
                    dataType: "json",
                    contentType: 'application/json',
                })
				alert("add success");
                this.addLoading = false;
                this.addFormVisible = false;
                this.getServices();
			},
			selsChange: function (sels) {
				this.sels = sels;
			}

		},
		mounted() {
			//this.getServicesMock();
			this.getServices();
		}
	}

</script>

<style scoped>

</style>

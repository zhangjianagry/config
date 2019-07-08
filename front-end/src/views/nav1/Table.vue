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
			<el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
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
			</el-table>
		</el-dialog>

		<!--config编辑界面-->
		<el-dialog title="edit config" v-model="editFormVisible" :close-on-click-modal="false">
			<el-form :model="objConfig" label-width="80px" :rules="editFormRules" ref="editForm">
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
			<el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
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
	</section>
</template>

<script>
    import $ from 'jquery'
	import util from '../../common/js/util'
	//import NProgress from 'nprogress'
	import { getServicesListPage, removeServ, batchRemoveServ, editServ, addServ } from '../../api/api';

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
				sels: [],//列表选中列
				listFormVisible: false,
				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
                    serviceID: [
						{ required: true, message: '请输入服务ID', trigger: 'blur' }
					]
				},
				configList:[],
                objConfig:[],
				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
                    serviceID: [
						{ required: true, message: '请输入ID', trigger: 'blur' }
					]
				},
				//新增界面数据
				addForm: {
					name: '',
					addr: ''
				},
				serviceID:''

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
                $.get('http://localhost:3100/getServices/1034541113705512871',function(data){
                    self.servs = data;
                    self.total = data.length;
                    self.listLoading = false;
				});
			},
			//删除
			handleDel: function (index, row) {
				this.$confirm('确认删除该记录吗?', '提示', {
					type: 'warning'
				}).then(() => {
					this.listLoading = true;
					//NProgress.start();
					let para = { id: row.id };
					removeServ(para).then((res) => {
						this.listLoading = false;
						//NProgress.done();
						this.$message({
							message: '删除成功',
							type: 'success'
						});
						this.getServices();
					});
				}).catch(() => {

				});
			},
            //批量删除
            batchRemove: function () {
                var ids = this.sels.map(item => item.id).toString();
                this.$confirm('确认删除选中记录吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    this.listLoading = true;
                    //NProgress.start();
                    let para = { ids: ids };
                    batchRemoveServ(para).then((res) => {
                        this.listLoading = false;
                        //NProgress.done();
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        this.getServices();
                    });
                }).catch(() => {

                });
            },
			//显示configList
			showConfig: function(index, row){
			    this.serviceID = Object.assign({}, row).server_id;
			    this.listFormVisible = true;
			    this.configList = Object.assign({}, row).config;
			},
			//编辑
			getConfig: function (cell) {
			    this.objConfig = cell;
                this.editFormVisible = true;
				// this.$refs.editForm.validate((valid) => {
				// 	if (valid) {
				// 		this.$confirm('确认提交吗？', '提示', {}).then(() => {
				// 			this.editLoading = true;
				// 			//NProgress.start();
				// 			let para = Object.assign({}, this.editForm);
				// 			para.time = (!para.time || para.time == '') ? '' : util.formatDate.format(new Date(para.time), 'yyyy-MM-dd');
				// 			editServ(para).then((res) => {
				// 				this.editLoading = false;
				// 				//NProgress.done();
				// 				this.$message({
				// 					message: '提交成功',
				// 					type: 'success'
				// 				});
				// 				this.$refs['editForm'].resetFields();
				// 				this.editFormVisible = false;
				// 				this.getServicesMock();
				// 			});
				// 		});
				// 	}
				// });
			},
            //显示新增界面
            handleAdd: function () {
                this.addFormVisible = true;
                this.addForm = {
                    name: '',
                    addr: ''
                };
            },
            configSubmit: function() {
				var config = {};
				var objConfig = this.objConfig;
				Object.keys(objConfig).forEach(function(key){
					config[key] = objConfig[key];
				});
                $.ajax({
                    url: "",
                    data: config,
                    type: "POST",
                    dataType: "json",
                    success: function(data) {
                        data = $.parseJSON(data);
                    }
                });

			},
			//新增service
			addSubmit: function () {
				this.$refs.addForm.validate((valid) => {
					if (valid) {
						this.$confirm('commit confirm?', 'instruction', {}).then(() => {
							this.addLoading = true;
							//NProgress.start();
							let para = Object.assign({}, this.addForm);
							para.time = (!para.time || para.time == '') ? '' : util.formatDate.format(new Date(para.time), 'yyyy-MM-dd');
							addServ(para).then((res) => {
								this.addLoading = false;
								//NProgress.done();
								this.$message({
									message: 'commit success',
									type: 'success'
								});
								this.$refs['addForm'].resetFields();
								this.addFormVisible = false;
								this.getServices();
							});
						});
					}
				});
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

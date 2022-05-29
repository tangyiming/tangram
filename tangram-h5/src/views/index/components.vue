<template>
    <div class="view-box">
        <div class="content-style">
            <div class="ant-advanced-search-form">
                <a-form>
                    <a-row>
                        <a-col :span="14">
                            <a-button type="primary" @click="handleAdd"> 新增</a-button>
                        </a-col>
                        <a-col :span="3">
                            <a-tooltip>
                                <template slot="title">组件状态</template>
                                <a-select v-model="search.status" style="width: 130px">
                                    <a-select-option value="">全部</a-select-option>
                                    <a-select-option value="0">待上架</a-select-option>
                                    <a-select-option value="1">已上架</a-select-option>
                                </a-select>
                            </a-tooltip>
                        </a-col>
                        <a-col :span="5">
                            <a-tooltip>
                                <template slot="title">组件名称</template>
                                <a-input v-model="search.name" style="width: 230px" />
                            </a-tooltip>
                        </a-col>
                        <a-col :span="2">
                            <a-button @click="handleQuery"> 搜索</a-button>
                        </a-col>
                    </a-row>
                </a-form>
            </div>
            <div style="margin-top: 20px"></div>
            <a-table :columns="columns" :data-source="tableData" row-key="id" style="margin-top: 20px">
                <template slot="id" slot-scope="text, record, index">
                    {{ parseInt(index) + 1 }}
                </template>
                <template slot="compType" slot-scope="record">
                    <span v-if="record.compType === 0">远程</span>
                    <span v-else-if="record.compType===2">代码</span>
                    <span v-else>本地</span>
                </template>
                <template slot="operation" slot-scope="record">
                    <a-tooltip>
                        <template slot="title"> 编辑 </template>
                        <a-button @click="handleEidt(record)" icon="edit" size="small" />
                    </a-tooltip>
                    <a-tooltip>
                        <template slot="title"> 执行 </template>
                        <a-button @click="handleExecute(record)" icon="caret-right" size="small" style="margin-left: 5px" />
                    </a-tooltip>
                    <a-tooltip>
                        <template slot="title"> 审核 </template>
                        <a-popconfirm title="确认变更组件审核状态?" ok-text="是" cancel-text="否" @confirm="handleAudit(record)" placement="bottom">
                            <a-button icon="eye" size="small" style="margin-left: 5px" />
                        </a-popconfirm>
                    </a-tooltip>
                    <a-tooltip>
                        <template slot="title"> 删除 </template>
                        <a-popconfirm title="确认删除?" ok-text="是" cancel-text="否" @confirm="confirmDelete(record)" placement="bottom">
                            <a-button icon="delete" size="small" style="margin-left: 5px" />
                        </a-popconfirm>
                    </a-tooltip>
                </template>
                <template slot="compStatus" slot-scope="record">
                    <a-tag v-if="record.compStatus === 0">待上架</a-tag>
                    <a-tag v-else>已上架</a-tag>
                </template>
                <template slot="createdBy" slot-scope="record"> {{ JSON.parse(record.createdBy).name }}&lt;{{ JSON.parse(record.createdBy).jobNumber }}&gt; </template>
                <template slot="bizId" slot-scope="record">
                    {{ record.bizLine.bizName }}
                </template>
            </a-table>
        </div>
        <a-modal :title="modalTile" :visible="visible" @ok="handleOk" @cancel="handleCancel" width="80%">
            <a-form-model :model="form" ref="form" :label-col="labelCol" :wrapper-col="wrapperCol">
                <a-form-model-item label="ID" ref="id" prop="id" v-show="mode === 'edit'">
                    <a-input v-model="form.id" disabled />
                </a-form-model-item>
                <a-form-model-item label="组件名" ref="compName" prop="compName">
                    <a-input v-model="form.compName" />
                </a-form-model-item>
                <a-form-model-item label="组件描述" ref="compDesc" prop="compDesc">
                    <a-textarea v-model="form.compDesc" />
                </a-form-model-item>
                <a-form-model-item label="类型" ref="compType" prop="compType">
                    <a-select v-model="form.compType">
                        <a-select-option :value="0"> 远程 </a-select-option>
                        <a-select-option :value="1"> 本地 </a-select-option>
                        <a-select-option :value="2"> 代码 </a-select-option>
                    </a-select>
                </a-form-model-item>
                <a-button @click="addParams('params')" type="primary" style="margin-left: 155px">新增入参</a-button>
                <a-form-model-item label="入参">
                    <div v-for="(v, i) in form.params" :key="i">
                        <span>
                            <a-input v-model="v.key" placeholder="参数名" style="width: 220px" />
                            <a-select v-model="v.require" placeholder="必填" style="width: 100px; margin-left: 5px">
                                <a-select-option :value="1">必填</a-select-option>
                                <a-select-option :value="0">可选</a-select-option>
                            </a-select>
                            <a-input v-model="v.value" placeholder="值" style="width: 260px; margin-left: 5px" />
                            <a-button shape="circle" icon="delete" type="danger" :style="{ marginLeft: '10px' }" @click="deleteParams(i, 'params')" />
                            <a-textarea v-model="v.desc" placeholder="说明" auto-size />
                        </span>
                    </div>
                </a-form-model-item>
                <a-button @click="addParams('output')" type="primary" style="margin-left: 155px">新增出参</a-button>
                <a-form-model-item label="出参">
                    <div v-for="(v, i) in form.output" :key="i">
                        <span>
                            <a-input v-model="v.key" placeholder="参数名" style="width: 220px" />
                            <a-button shape="circle" icon="delete" type="danger" :style="{ marginLeft: '10px' }" @click="deleteParams(i, 'output')" />
                            <a-textarea v-model="v.desc" placeholder="说明" auto-size />
                        </span>
                    </div>
                </a-form-model-item>
                <a-form-model-item label="路径" ref="urlpath" prop="urlpath" v-show="form.compType === 0">
                    <a-input v-model="form.urlpath" />
                </a-form-model-item>
                <a-form-model-item label="类名" ref="className" prop="className" v-show="form.compType === 1">
                    <a-input v-model="form.className" />
                </a-form-model-item>
                <a-form-model-item label="代码" ref="code" prop="code" v-show="form.compType === 2">
                    <a-textarea v-model="form.code"></a-textarea>
                </a-form-model-item>
                <a-form-model-item label="所属业务" ref="bizId" prop="bizId">
                    <a-select v-model="form.bizId">
                        <a-select-option v-for="item in bizLines" :key="item.id">{{ item.bizName }}</a-select-option>
                    </a-select>
                </a-form-model-item>
            </a-form-model>
        </a-modal>
        <a-back-top />
    </div>
</template>
<script>
import { cmpAdd, cmpUpdate, cmpQuery, cmpDel, cmpExec, cmpUpdateStatus } from '@/requests/components'
import { bizList } from '@/requests/bizlines'
import { mapState } from 'vuex'

const columns = [
    {
        scopedSlots: { customRender: 'id' },
    },
    {
        title: '组件名',
        key: 'compName',
        dataIndex: 'compName',
    },
    {
        title: '描述',
        key: 'compDesc',
        dataIndex: 'compDesc',
    },
    {
        title: '类型',
        key: 'compType',
        scopedSlots: { customRender: 'compType' },
    },
    {
        title: '业务域',
        key: 'bizId',
        scopedSlots: { customRender: 'bizId' },
    },
    {
        title: '状态',
        key: 'compStatus',
        scopedSlots: { customRender: 'compStatus' },
    },
    {
        title: '创建人',
        key: 'createdBy',
        scopedSlots: { customRender: 'createdBy' },
    },
    {
        title: '操作',
        key: 'operation',
        width: '150px',
        scopedSlots: { customRender: 'operation' },
    },
]

export default {
    name: 'components',
    data() {
        return {
            search: { status: '', name: '' },
            columns,
            tableData: [],
            visible: false,
            mode: 'add',
            modalTile: '新增组件',
            labelCol: { span: 4 },
            wrapperCol: { span: 16 },
            form: {
                id: undefined,
                compName: undefined,
                compDesc: undefined,
                compType: undefined,
                params: [],
                output: [],
                urlpath: undefined,
                className: undefined,
                code:undefined,
                bizId: undefined,
                compStatus: undefined,
                createTime: undefined,
                updateTime: undefined,
                createdBy: undefined,
            },
            bizLines: [],
        }
    },
    computed: {
        ...mapState({
            loginUser: state => state.loginUser,
        }),
    },
    mounted() {
        this.handleQuery(this.search)
        bizList().then(res => {
            if (res.result === 1) {
                this.bizLines = res.data
            }
        })
    },
    methods: {
        handleAudit(val) {
            cmpUpdateStatus(val).then(res => {
                if (res.result === 1) {
                    this.$message.success('变更成功')
                    this.handleQuery()
                } else {
                    this.$message.error('变更失败')
                }
            })
        },
        confirmDelete(val) {
            if (val.compStatus === 1) {
                this.$message.warn('已上架组件不支持删除')
            } else {
                cmpDel(val).then(res => {
                    if (res.result === 1) {
                        this.$message.success('成功删除')
                        this.handleQuery()
                    } else {
                        this.$message.error('删除失败')
                    }
                })
            }
        },
        handleAdd() {
            this.mode = 'add'
            this.visible = true
            this.modalTile = '新增组件'
        },
        handleEidt(val) {
            this.mode = 'edit'
            this.visible = true
            this.modalTile = '编辑组件'
            this.form = { ...val }
            this.form.params = JSON.parse(val.params)
            this.form.output = JSON.parse(val.output)
        },
        handleExecute(val) {
            val.output = JSON.parse(val.output)
            val.params = JSON.parse(val.params)
            cmpExec(val).then(res => {
                this.$message.success(JSON.stringify(res.data))
                val.output = JSON.stringify(val.output)
                val.params = JSON.stringify(val.params)
            })
        },
        handleOk() {
            if (this.mode === 'add') {
                let u = { name: this.loginUser.name, jobNumber: this.loginUser.jobNumber }
                let p = { ...this.form, params: JSON.stringify(this.form.params), output: JSON.stringify(this.form.output), createdBy: JSON.stringify(u), id: undefined, createTime: undefined }
                cmpAdd(p).then(res => {
                    if (res.result === 1) {
                        this.$message.success('添加成功')
                        this.visible = false
                        this.handleQuery()
                    }
                })
            } else {
                let u = { name: this.loginUser.name, jobNumber: this.loginUser.jobNumber }
                let p = { ...this.form, params: JSON.stringify(this.form.params), output: JSON.stringify(this.form.output), createdBy: JSON.stringify(u) }
                cmpUpdate(p).then(res => {
                    if (res.result === 1) {
                        this.$message.success('更新成功')
                        this.visible = false
                        this.handleQuery()
                    }
                })
            }
        },
        handleCancel() {
            this.visible = false
            this.$refs.form.resetFields()
            this.form.output = []
            this.form.params = []
        },
        handleQuery() {
            cmpQuery(this.search).then(res => {
                if (res.result === 1) {
                    this.tableData = res.data
                }
            })
        },
        addParams(k) {
            this.form[k].push({ key: '', value: '', desc: '' })
        },
        deleteParams(index, k) {
            this.form[k].splice(index, 1)
        },
    },
}
</script>

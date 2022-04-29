<template>
    <div class="view-box">
        <div class="content-style">
            <div class="ant-advanced-search-form">
                <a-form>
                    <a-row>
                        <a-col>
                            <a-button type="primary" @click="handleAdd"> 新增</a-button>
                        </a-col>
                    </a-row>
                </a-form>
            </div>
            <a-table :columns="columns" :data-source="tableData" row-key="id" style="margin-top: 20px">
                <template slot="id" slot-scope="text, record, index">
                    {{ parseInt(index) + 1 }}
                </template>
                <template slot="charger" slot-scope="record">
                    <div v-for="(v, i) in JSON.parse(record.charger)" :key="i">
                        <span>{{ v.name }}&lt;{{ v.jobNumber }}&gt; / {{ v.tel }}</span>
                    </div>
                </template>
                <template slot="baseUrl" slot-scope="record">
                    {{ !!!record.baseUrl ? '-' : record.baseUrl }}
                </template>
                <template slot="authInfo" slot-scope="record">
                    {{ !!!record.authInfo ? '-' : record.authInfo }}
                </template>
                <template slot="operation" slot-scope="record">
                    <a-tooltip>
                        <template slot="title"> 编辑 </template>
                        <a-button @click="handleEidt(record)" icon="edit" size="small" />
                    </a-tooltip>
                    <a-tooltip>
                        <template slot="title"> 删除 </template>
                        <a-popconfirm title="确认删除?" ok-text="是" cancel-text="否" @confirm="confirmDelete(record)" placement="bottom">
                            <a-button icon="delete" size="small" style="margin-left: 5px" />
                        </a-popconfirm>
                    </a-tooltip>
                </template>
            </a-table>
        </div>
        <a-modal :title="modalTile" :visible="visible" @ok="handleOk" @cancel="handleCancel">
            <a-form-model :model="form" ref="form" :label-col="labelCol" :wrapper-col="wrapperCol">
                <a-form-model-item label="ID" ref="id" prop="id" v-show="mode === 'edit'">
                    <a-input v-model="form.id" disabled />
                </a-form-model-item>
                <a-form-model-item label="业务线名称" ref="bizName" prop="bizName">
                    <a-input v-model="form.bizName" />
                </a-form-model-item>
                <a-form-model-item label="业务线编码" ref="bizCode" prop="bizCode">
                    <a-input v-model="form.bizCode" />
                </a-form-model-item>
                <a-form-model-item label="基础url" ref="baseUrl" prop="baseUrl">
                    <a-input v-model="form.baseUrl" />
                </a-form-model-item>
                <a-form-model-item label="负责人" ref="charger" prop="charger">
                    <a-input v-model="form.charger" />
                </a-form-model-item>
                <a-form-model-item label="认证设置" ref="authInfo" prop="authInfo">
                    <a-input v-model="form.authInfo" />
                </a-form-model-item>
            </a-form-model>
        </a-modal>
        <a-back-top />
    </div>
</template>

<script>
import { bizAdd, bizList, bizUpdate, bizDelete } from '@/requests/bizlines'

const columns = [
    {
        scopedSlots: { customRender: 'id' },
    },
    {
        title: '业务线名称',
        key: 'bizName',
        dataIndex: 'bizName',
    },
    {
        title: '业务线编码',
        key: 'bizCode',
        dataIndex: 'bizCode',
    },
    {
        title: '基础url',
        key: 'baseUrl',
        scopedSlots: { customRender: 'baseUrl' },
    },
    {
        title: '认证设置',
        key: 'authInfo',
        scopedSlots: { customRender: 'authInfo' },
    },
    {
        title: '负责人',
        key: 'charger',
        scopedSlots: { customRender: 'charger' },
    },
    {
        title: '操作',
        key: 'operation',
        scopedSlots: { customRender: 'operation' },
    },
]

export default {
    name: 'bizlines',
    data() {
        return {
            columns,
            tableData: [],
            visible: false,
            mode: 'add',
            modalTile: '新增业务线信息',
            labelCol: { span: 4 },
            wrapperCol: { span: 16 },
            form: {
                id: undefined,
                bizName: undefined,
                bizCode: undefined,
                baseUrl: undefined,
                charger: undefined,
                authInfo: undefined,
            },
        }
    },
    mounted() {
        this.handleList()
    },
    methods: {
        confirmDelete(val) {
            bizDelete(val).then(res => {
                if (res.result === 1) {
                    this.$message.success('删除成功')
                    this.handleList()
                } else {
                    this.$message.error('删除失败')
                }
            })
        },
        handleAdd() {
            this.mode = 'add'
            this.visible = true
            this.modalTile = '新增业务线信息'
        },
        handleEidt(val) {
            this.mode = 'edit'
            this.visible = true
            this.modalTile = '编辑业务线信息'
            this.form = val
        },
        handleOk() {
            if (this.mode === 'add') {
                bizAdd(this.form).then(res => {
                    if (res.result === 1) {
                        this.$message.success('添加成功')
                        this.visible = false
                        this.handleList()
                    }
                })
            } else {
                bizUpdate(this.form).then(res => {
                    if (res.result === 1) {
                        this.$message.success('更新成功')
                        this.visible = false
                        this.handleList()
                    }
                })
            }
        },
        handleCancel() {
            this.visible = false
            this.$refs.form.resetFields()
        },
        handleList() {
            bizList().then(res => {
                if (res.result === 1) {
                    this.tableData = res.data
                }
            })
        },
    },
}
</script>

<style scoped></style>

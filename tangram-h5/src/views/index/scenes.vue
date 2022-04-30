<template>
    <div class="view-box">
        <div class="content-style">
            <div class="ant-advanced-search-form" v-show="visible">
                <a-form>
                    <a-row>
                        <a-col :span="14">
                            <a-button type="primary" @click="handleAdd"> 新增</a-button>
                        </a-col>
                        <a-col :span="3">
                            <a-tooltip>
                                <template slot="title">场景状态</template>
                                <a-select v-model="search.status" style="width: 130px">
                                    <a-select-option value="">全部</a-select-option>
                                    <a-select-option value="0">待上架</a-select-option>
                                    <a-select-option value="1">已上架</a-select-option>
                                </a-select>
                            </a-tooltip>
                        </a-col>
                        <a-col :span="5">
                            <a-tooltip>
                                <template slot="title">场景名称</template>
                                <a-input v-model="search.name" style="width: 230px" />
                            </a-tooltip>
                        </a-col>
                        <a-col :span="2">
                            <a-button @click="handleQuery"> 搜索</a-button>
                        </a-col>
                    </a-row>
                </a-form>
            </div>
            <div v-show="visible">
                <a-table :columns="columns" :data-source="scenes" row-key="id">
                    <template slot="id" slot-scope="text, record, index">
                        {{ parseInt(index) + 1 }}
                    </template>
                    <template slot="bizLine" slot-scope="record">
                        {{ record.bizLine.bizName }}
                    </template>
                    <template slot="sceneStatus" slot-scope="record">
                        <a-tag v-if="record.sceneStatus === 0">待上架</a-tag>
                        <a-tag v-else>已上架</a-tag>
                    </template>
                    <template slot="createdBy" slot-scope="record"> {{ JSON.parse(record.createdBy).name }}&lt;{{ JSON.parse(record.createdBy).jobNumber }}&gt; </template>
                    <template slot="operation" slot-scope="record">
                        <a-tooltip>
                            <template slot="title"> 编辑 </template>
                            <a-button icon="edit" size="small" @click="handleClick(record)" />
                        </a-tooltip>
                        <a-tooltip>
                            <template slot="title"> 执行 </template>
                            <a-button @click="excuteSceneOutSide(record)" icon="caret-right" size="small" style="margin-left: 5px" />
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
                </a-table>
            </div>
            <div v-show="!visible" style="height: 100%">
                <a-button size="small" @click="quitSceneAdd" icon="arrow-left">返回</a-button>
                <div style="display: flex; justify-content: stretch; height: calc(100% - 25px); margin-top: 5px">
                    <div style="flex: 1; background-color: #f6f1f1; padding: 10px">
                        <a-input-search placeholder="TODO：输入组件名搜索" />
                        <Container behaviour="copy" group-name="1" :get-child-payload="i => getChildPayload1(i, k)" v-for="(v, k) in comps" :key="k">
                            <div style="margin-top: 10px; cursor: pointer" @click="handleExpand"><a-icon type="caret-down" />{{ k }}</div>
                            <Draggable v-for="(cmp, index) in comps[k]" :key="index">
                                <div class="cmp-list">
                                    {{ cmp.compName }}
                                </div>
                            </Draggable>
                        </Container>
                    </div>
                    <div style="margin-left: 10px; flex: 4; background-color: #f6f1f1; padding: 10px">
                        <div>
                            <a-select v-model="bizId" style="width: 250px" placeholder="请选择所属业务域">
                                <a-select-option v-for="item in bizLines" :key="item.id">{{ item.bizName }}</a-select-option>
                            </a-select>
                            <a-input placeholder="请输入场景名" v-model="name" style="margin-top: 5px" />
                            <a-textarea placeholder="请输入场景描述" v-model="desc" style="margin-top: 5px" />
                        </div>
                        <div style="margin-top: 5px; padding: 5px; background-color: #fdf3a1; border-radius: 3px">
                            <div style="background-color: #e7e7e7; margin-bottom: 5px">
                                <a-button @click="addParams" type="primary" style="">新增场景参数</a-button>
                                <div v-for="(v, i) in commonParams" :key="i">
                                    <span>
                                        <a-input v-model="v.key" placeholder="参数名" style="width: 220px" />
                                        <a-select v-model="v.require" placeholder="必填" style="width: 100px; margin-left: 5px">
                                            <a-select-option :value="1">必填</a-select-option>
                                            <a-select-option :value="0">可选</a-select-option>
                                        </a-select>
                                        <a-input v-model="v.value" placeholder="值" style="width: 260px; margin-left: 5px" />
                                        <a-button shape="circle" icon="delete" type="danger" :style="{ marginLeft: '10px' }" @click="deleteParams(i)" />
                                        <a-textarea v-model="v.desc" placeholder="说明" auto-size />
                                    </span>
                                </div>
                            </div>
                            <Container
                                group-name="1"
                                :get-child-payload="i => getChildPayload2(i)"
                                @drop="onDrop('scene', $event)"
                                :remove-on-drop-out="true"
                                style="padding-bottom: 5px; border: 2px dashed rgb(150, 147, 147)"
                            >
                                <Draggable v-for="(cmp, index) in scene" :key="index">
                                    <div class="cmp-scene">
                                        <div v-for="(v, i) in cmp.params" :key="i">
                                            <span style="display: flex">
                                                <a-input v-model="v.key" placeholder="参数名" style="width: 150px" disabled />
                                                <a-input v-model="v.desc" placeholder="说明" style="width: 200px; margin-left: 5px" disabled />
                                                <a-select v-model="v.require" placeholder="必填" style="width: 100px; margin-left: 5px" disabled>
                                                    <a-select-option :value="1">必填</a-select-option>
                                                    <a-select-option :value="0">可选</a-select-option>
                                                </a-select>
                                                <a-input v-model="v.value" placeholder="值" style="width: 200px; margin-left: 5px" @change="handleValChange(index, i)" />
                                                <a-select style="width: 200px" @focus="deleteAfter(index)" @change="v => handleMapChange(v, index, i)" v-model="v.mapping">
                                                    {{<a-select-option v-for="c in showChoices" :key="c">{{ c }}</a-select-option>
                                                </a-select>
                                            </span>
                                        </div>
                                        <div style="background-color: #cdd7e5">{{ index + ' - ' + cmp.compName }}</div>
                                        <div v-for="(v, i) in cmp.output" :key="i + 100">
                                            <span style="display: flex">
                                                <a-input v-model="v.key" placeholder="参数名" style="width: 150px" disabled />
                                                <a-input v-model="v.desc" placeholder="说明" style="width: 200px; margin-left: 5px" disabled />
                                            </span>
                                        </div>
                                    </div>
                                </Draggable>
                                <div style="text-align: center; height: 60px; margin-top: 40px" v-if="scene.length === 0">
                                    <h1>组件组装区域，请将左侧组件拖到此处</h1>
                                </div>
                            </Container>
                        </div>
                        <div style="display: flex; margin-top: 5px">
                            <a-button @click="excuteScene">执行</a-button>
                            <a-button @click="saveScene" style="margin-left: 5px">保存</a-button>
                        </div>
                        <div style="background-color: #e7e7e7; margin-top: 10px; margin-bottom: 5px; height: 80px; text-align: center">
                            <h2 style="">TODO：执行过程日志展示区域</h2>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { bizList } from '@/requests/bizlines'
import { cmpQuery } from '@/requests/components'
import { sceneAdd, sceneQuery, sceneExec, sceneDel, sceneUpdateStatus, sceneUpdate } from '@/requests/scenes'
import { mapState } from 'vuex'
import { applyDrag } from '@/utils/helper'
import { Container, Draggable } from 'vue-smooth-dnd'

const columns = [
    {
        scopedSlots: { customRender: 'id' },
    },
    {
        title: '场景名',
        key: 'sceneName',
        dataIndex: 'sceneName',
    },
    {
        title: '描述',
        key: 'sceneDesc',
        dataIndex: 'sceneDesc',
    },
    {
        title: '业务域',
        key: 'bizLine',
        scopedSlots: { customRender: 'bizLine' },
    },
    {
        title: '状态',
        key: 'sceneStatus',
        scopedSlots: { customRender: 'sceneStatus' },
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
    name: 'scenes',
    components: { Container, Draggable },
    data() {
        return {
            mode: 'add',
            visible: true,
            search: { status: '', name: '' },
            bizLines: [],
            comps: {},
            scenes: [],
            columns,
            scene: [],
            choices: [],
            showChoices: [],
            name: undefined,
            desc: undefined,
            bizId: undefined,
            sceneId: undefined,
            createdBy: undefined,
            commonParams: [],
            rowClick: record => ({
                on: {
                    click: () => {
                        this.handleClick(record)
                    },
                },
            }),
        }
    },
    computed: {
        ...mapState({
            loginUser: state => state.loginUser,
        }),
    },
    mounted() {
        bizList().then(res => {
            if (res.result === 1) {
                this.bizLines = res.data
            }
        })

        cmpQuery(this.search).then(res => {
            if (res.result === 1) {
                res.data.forEach(cmp => {
                    if (!this.comps.hasOwnProperty(cmp.bizLine.bizName)) {
                        this.comps[cmp.bizLine.bizName] = []
                    }
                    this.comps[cmp.bizLine.bizName].push({ ...cmp, params: JSON.parse(cmp.params), output: JSON.parse(cmp.output) })
                })
            }
        })

        this.handleQuery()
    },
    methods: {
        handleAdd() {
            this.visible = false
            this.mode = 'add'
            this.name = undefined
            this.desc = undefined
            this.bizId = undefined
            this.sceneId = undefined
            this.createdBy = undefined
            this.commonParams = []
            this.scene = []
        },
        excuteSceneOutSide(val) {
            this.scene = JSON.parse(val.flowData)
            this.commonParams = JSON.parse(val.commonParams)
            this.excuteScene()
        },
        handleAudit(val) {
            sceneUpdateStatus(val).then(res => {
                if (res.result === 1) {
                    this.$message.success('变更成功')
                    this.handleQuery()
                } else {
                    this.$message.error('变更失败')
                }
            })
        },
        handleQuery() {
            sceneQuery(this.search).then(res => {
                if (res.result === 1) {
                    this.scenes = res.data
                }
            })
        },
        handleExpand() {
            // todo
        },
        confirmDelete(val) {
            if (val.sceneStatus === 1) {
                this.$message.warn('已上架造数场景不支持删除')
            } else {
                sceneDel(val).then(res => {
                    if (res.result === 1) {
                        this.$message.success('成功删除')
                        this.handleQuery()
                    } else {
                        this.$message.error('删除失败')
                    }
                })
            }
        },
        getChildPayload1(index, k) {
            return this.comps[k][index - 1]
        },
        getChildPayload2(index) {
            return this.scene[index]
        },
        onDrop(collection, dropResult) {
            this[collection] = applyDrag(this[collection], dropResult)
            this.choices = []
            this.scene.forEach((cmp, index) => {
                cmp.output.forEach(out => {
                    this.choices.push(index + ':' + out.key)
                })
            })
        },
        deleteAfter(idx) {
            this.showChoices = []
            this.commonParams.forEach(cp => {
                // SP SeceneParams 场景参数
                this.showChoices.push('SP:' + cp.key)
            })
            let tmp = JSON.parse(JSON.stringify(this.choices))
            tmp.forEach(i => {
                i.split(':')[0] < idx ? this.showChoices.push(i) : null
            })
        },
        handleMapChange(val, index, i) {
            this.scene[index].params[i].value = undefined
            this.scene[index].params[i].mapping = val
        },
        handleValChange(index, i) {
            this.scene[index].params[i].mapping = undefined
        },
        excuteScene() {
            sceneExec({ flowData: this.scene, commonParams: this.commonParams }).then(res => {
                if (res.result === 1) {
                    this.$message.success(res.data)
                }
            })
        },
        saveScene() {
            if (this.mode === 'add') {
                let u = { name: this.loginUser.name, jobNumber: this.loginUser.jobNumber }
                let p = {
                    sceneName: this.name,
                    sceneDesc: this.desc,
                    bizlineId: this.bizId,
                    flowData: JSON.stringify(this.scene),
                    commonParams: JSON.stringify(this.commonParams),
                    createdBy: JSON.stringify(u),
                }
                sceneAdd(p)
                    .then(res => {
                        res.result === 1 ? this.$message.success('新增成功') : this.$message.error('新增失败')
                    })
                    .finally(() => {
                        this.quitSceneAdd()
                    })
            } else {
                let p = {
                    id: this.sceneId,
                    sceneName: this.name,
                    sceneDesc: this.desc,
                    bizlineId: this.bizId,
                    flowData: JSON.stringify(this.scene),
                    commonParams: JSON.stringify(this.commonParams),
                    createdBy: this.createdBy,
                }
                sceneUpdate(p)
                    .then(res => {
                        res.result === 1 ? this.$message.success('更新成功') : this.$message.error('更新失败')
                    })
                    .finally(() => {
                        this.quitSceneAdd()
                    })
            }
        },
        handleClick(val) {
            this.mode = 'edit'
            this.name = val.sceneName
            this.desc = val.sceneDesc
            this.bizId = val.bizlineId
            this.createdBy = val.createdBy
            this.sceneId = val.id
            this.scene = JSON.parse(val.flowData)
            this.commonParams = JSON.parse(val.commonParams)
            this.choices = []
            this.scene.forEach((cmp, index) => {
                cmp.output.forEach(out => {
                    this.choices.push(index + ':' + out.key)
                })
            })
            this.visible = false
        },
        quitSceneAdd() {
            this.visible = true
            this.scene = []
            this.handleQuery()
        },
        addParams() {
            this.commonParams.push({ key: '', value: '', desc: '' })
        },
        deleteParams(index) {
            this.commonParams.splice(index, 1)
        },
    },
}
</script>

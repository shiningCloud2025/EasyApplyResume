import{r as x,j as e}from"./index-6ccb5bf7.js";import{r as m}from"./react-vendor-fa9da516.js";import{o as v,U as A,aH as w,B as D,a8 as N,I as C,m as O}from"./antd-vendor-40a3c64f.js";const R={applicationChat:(a,n)=>x.post("/user/aiResumeAssistant/application/chat",a,{params:{chatId:n},headers:{Accept:"text/event-stream"}}),agentChat:(a,n)=>x.post("/user/aiResumeAssistant/agent/chat",a,{params:{chatId:n},headers:{Accept:"text/event-stream"}})},{TextArea:M}=C,L=()=>{const[a,n]=m.useState(!1),[o,h]=m.useState(""),[f,S]=m.useState(),[y,p]=m.useState([{id:1,type:"assistant",content:`您好！我是您的AI求职助手，我可以帮助您：

📝 优化简历内容
💡 提供求职建议
📊 分析就业市场
🎯 制定职业规划

请问有什么可以帮助您的吗？`,timestamp:new Date().toISOString()}]),g=async()=>{if(!o.trim())return;const t={id:Date.now(),type:"user",content:o,timestamp:new Date().toISOString()};p(i=>[...i,t]);const s=o;h(""),n(!0);try{const u=(await R.applicationChat(s,f)).data.split(`
`);let l="";for(const r of u)if(r.startsWith("data: ")){const c=r.slice(6);if(c==="[DONE]")break;try{const d=JSON.parse(c);d.content&&(l+=d.content),d.chatId&&S(d.chatId)}catch{}}if(l.trim()){const r={id:Date.now()+1,type:"assistant",content:l,timestamp:new Date().toISOString()};p(c=>[...c,r])}else{const r={id:Date.now()+1,type:"assistant",content:I(s),timestamp:new Date().toISOString()};p(c=>[...c,r])}}catch(i){console.error("AI API调用失败:",i),O.error("AI暂时无法响应，请稍后重试");const u={id:Date.now()+1,type:"assistant",content:I(s),timestamp:new Date().toISOString()};p(l=>[...l,u])}finally{n(!1)}},I=t=>{const s=t.toLowerCase();return s.includes("简历")||s.includes("cv")?`关于简历制作，我建议您注意以下几点：

🎯 **要点突出**
- 将最重要的技能和经验放在前面
- 使用数字和成果量化您的成就
- 避免使用模糊的描述

✨ **格式优化**
- 保持简洁，1-2页最佳
- 使用清晰的字体和适当的间距
- 确保没有语法和拼写错误

📊 **内容匹配**
- 根据应聘职位调整内容
- 突出与职位相关的技能和经历
- 研究公司文化和要求

需要我帮您检查具体的简历内容吗？`:s.includes("面试")||s.includes("interview")?`面试准备攻略：

🔍 **前期准备**
- 深入研究公司和职位要求
- 准备常见面试问题的答案
- 练习STAR法则的案例回答

💼 **面试当天**
- 提前10分钟到达
- 着装得体，保持礼貌
- 带上简历和其他必要文件

🎤 **面试技巧**
- 保持眼神交流
- 认真倾听问题
- 展现您的热情和学习能力
- 准备一些有深度的问题问到面试官

有具体的面试需要准备吗？`:s.includes("薪资")||s.includes("薪资谈判")||s.includes("salary")?`薪资谈判技巧：

💰 **前期调研**
- 了解行业同等职位的薪资范围
- 考虑城市生活成本
- 评估自身经验和能力价值

📈 **谈判策略**
- 不要第一个出价
- 给出薪资范围而不是具体数字
- 强调您能带来的价值

🎯 **综合考量**
- 不仅看基本工资
- 考虑奖金、股票、福利等
- 重视职业发展机会

需要我帮您分析具体的薪资情况吗？`:`我理解您的问题。作为AI求职助手，我建议您：

📚 **持续学习**
- 不断提升专业技能
- 学习新技术和工具
- 保持对行业趋势的关注

🌟 **个人品牌**
- 完善LinkedIn等专业档案
- 积极参与行业活动
- 建立专业人脉网络

🚀 **求职策略**
- 制定明确的求职目标
- 多渠道投递简历
- 定期跟踪和反馈

还有其他我可以帮助您的吗？`},j=t=>{t.key==="Enter"&&!t.shiftKey&&(t.preventDefault(),g())};return e.jsx("div",{className:"ai-assistant-page",children:e.jsxs(v,{className:"chat-container",title:"AI求职助手",children:[e.jsx("div",{className:"chat-messages",children:y.map(t=>e.jsxs("div",{className:`message ${t.type}`,children:[e.jsx("div",{className:"message-avatar",children:t.type==="user"?e.jsx(A,{}):e.jsx(w,{})}),e.jsxs("div",{className:"message-content",children:[e.jsx("div",{className:"message-text",children:t.content.split(`
`).map((s,i)=>e.jsx("p",{children:s},i))}),e.jsx("div",{className:"message-time",children:new Date(t.timestamp).toLocaleTimeString("zh-CN",{hour:"2-digit",minute:"2-digit"})})]})]},t.id))}),e.jsxs("div",{className:"chat-input",children:[e.jsx(M,{value:o,onChange:t=>h(t.target.value),onKeyPress:j,placeholder:"请输入您的问题...",rows:3,disabled:a}),e.jsx(D,{type:"primary",icon:e.jsx(N,{}),onClick:g,loading:a,disabled:!o.trim(),children:"发送"})]})]})})};export{L as default};

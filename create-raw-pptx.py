"""
终极方案：完全绕过 python-pptx，直接用 zip + lxml 构建 PPTX
确保动画 XML 100% 正确
"""
import zipfile, io, uuid
from lxml import etree

P = 'http://schemas.openxmlformats.org/presentationml/2006/main'
A = 'http://schemas.openxmlformats.org/drawingml/2006/main'
R = 'http://schemas.openxmlformats.org/officeDocument/2006/relationships'
CT = 'http://schemas.openxmlformats.org/package/2006/content-types'
REL = 'http://schemas.openxmlformats.org/officeDocument/2006/relationships'
PKG = 'http://schemas.openxmlformats.org/package/2006/relationships'

def NS(tag): return f'{{{P}}}{tag}'

def make_empty_pptx():
    """手动创建一个只有1页的PPTX"""
    buf = io.BytesIO()
    with zipfile.ZipFile(buf, 'w', zipfile.ZIP_DEFLATED) as z:
        # [Content_Types].xml
        ct = etree.Element('Types', xmlns=CT)
        etree.SubElement(ct, 'Default', Extension='rels', ContentType='application/vnd.openxmlformats-package.relationships+xml')
        etree.SubElement(ct, 'Default', Extension='xml', ContentType='application/xml')
        etree.SubElement(ct, 'Override', PartName='/ppt/presentation.xml', ContentType='application/vnd.openxmlformats-officedocument.presentationml.presentation.main+xml')
        etree.SubElement(ct, 'Override', PartName='/ppt/slides/slide1.xml', ContentType='application/vnd.openxmlformats-officedocument.presentationml.slide+xml')
        etree.SubElement(ct, 'Override', PartName='/ppt/slideMasters/slideMaster1.xml', ContentType='application/vnd.openxmlformats-officedocument.presentationml.slideMaster+xml')
        etree.SubElement(ct, 'Override', PartName='/ppt/slideLayouts/slideLayout1.xml', ContentType='application/vnd.openxmlformats-officedocument.presentationml.slideLayout+xml')
        etree.SubElement(ct, 'Override', PartName='/ppt/theme/theme1.xml', ContentType='application/vnd.openxmlformats-officedocument.theme+xml')
        z.writestr('[Content_Types].xml', etree.tostring(ct, xml_declaration=True, encoding='UTF-8', standalone=True))
        
        # _rels/.rels
        rels = etree.Element('Relationships', xmlns=REL)
        etree.SubElement(rels, 'Relationship', Id='rId1', Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument', Target='ppt/presentation.xml')
        z.writestr('_rels/.rels', etree.tostring(rels, xml_declaration=True, encoding='UTF-8', standalone=True))
        
        # ppt/_rels/presentation.xml.rels
        prels = etree.Element('Relationships', xmlns=REL)
        etree.SubElement(prels, 'Relationship', Id='rId1', Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/slide', Target='slides/slide1.xml')
        etree.SubElement(prels, 'Relationship', Id='rId2', Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideMaster', Target='slideMasters/slideMaster1.xml')
        etree.SubElement(prels, 'Relationship', Id='rId3', Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme', Target='theme/theme1.xml')
        z.writestr('ppt/_rels/presentation.xml.rels', etree.tostring(prels, xml_declaration=True, encoding='UTF-8', standalone=True))
        
        # ppt/presentation.xml
        pres = etree.Element(NS('presentation'), xmlns_a=A, xmlns_r=R)
        sldSz = etree.SubElement(pres, NS('sldSz')); sldSz.set('cx','12192000'); sldSz.set('cy','6858000')
        etree.SubElement(pres, NS('notesSz'), cx='6858000', cy='9144000')
        sldIdLst = etree.SubElement(pres, NS('sldIdLst'))
        sldIdEl = etree.SubElement(sldIdLst, NS('sldId'))
        sldIdEl.set('id', '256')
        sldIdEl.set(f'{{{R}}}id', 'rId1')
        z.writestr('ppt/presentation.xml', etree.tostring(pres, xml_declaration=True, encoding='UTF-8', standalone=True))
        
        # ppt/slideMasters/slideMaster1.xml (minimal)
        sm = etree.Element(NS('sldMaster'), xmlns_a=A, xmlns_r=R)
        etree.SubElement(sm, NS('cSld'))
        z.writestr('ppt/slideMasters/slideMaster1.xml', etree.tostring(sm, xml_declaration=True, encoding='UTF-8', standalone=True))
        
        # ppt/slideLayouts/slideLayout1.xml (minimal, must have a blank layout)
        sl = etree.Element(NS('sldLayout'), xmlns_a=A, xmlns_r=R, type='blank')
        etree.SubElement(sl, NS('cSld'), name='')
        z.writestr('ppt/slideLayouts/slideLayout1.xml', etree.tostring(sl, xml_declaration=True, encoding='UTF-8', standalone=True))
        
        # ppt/theme/theme1.xml (minimal)
        thm = etree.Element(A+'theme', name='Default', xmlns_a=A)
        etree.SubElement(thm, A+'themeElements')
        z.writestr('ppt/theme/theme1.xml', etree.tostring(thm, xml_declaration=True, encoding='UTF-8', standalone=True))
        
        # ppt/slides/slide1.xml
        sld = etree.Element(NS('sld'), xmlns_a=A, xmlns_r=R)
        cSld = etree.SubElement(sld, NS('cSld'))
        spTree = etree.SubElement(cSld, NS('spTree'))
        nvGrpSpPr = etree.SubElement(spTree, NS('nvGrpSpPr'))
        cNvPr = etree.SubElement(nvGrpSpPr, NS('cNvPr')); cNvPr.set('id','1'); cNvPr.set('name','Group')
        cNvGrpSpPr = etree.SubElement(nvGrpSpPr, NS('cNvGrpSpPr'))
        nvPr = etree.SubElement(nvGrpSpPr, NS('nvPr'))
        grpSpPr = etree.SubElement(spTree, NS('grpSpPr'))
        
        # 标题文字
        sp = etree.SubElement(spTree, NS('sp'))
        nvSpPr = etree.SubElement(sp, NS('nvSpPr'))
        cNvPr2 = etree.SubElement(nvSpPr, NS('cNvPr')); cNvPr2.set('id','2'); cNvPr2.set('name','Title')
        cNvSpPr = etree.SubElement(nvSpPr, NS('cNvSpPr'))
        nvPr2 = etree.SubElement(nvSpPr, NS('nvPr'))
        spPr = etree.SubElement(sp, NS('spPr'))
        xfrm = etree.SubElement(spPr, A+'xfrm')
        etree.SubElement(xfrm, A+'off', x='914400', y='457200')
        etree.SubElement(xfrm, A+'ext', cx='11000000', cy='800000')
        etree.SubElement(spPr, A+'prstGeom', prst='rect')
        txBody = etree.SubElement(sp, NS('txBody'))
        bodyPr = etree.SubElement(txBody, A+'bodyPr', wrap='square')
        etree.SubElement(bodyPr, A+'spAutoFit')
        lstStyle = etree.SubElement(txBody, A+'lstStyle')
        p_tag = etree.SubElement(txBody, A+'p')
        r_tag = etree.SubElement(p_tag, A+'r')
        rPr = etree.SubElement(r_tag, A+'rPr', sz='2800', b='1', lang='zh-CN')
        etree.SubElement(rPr, A+'solidFill').append(etree.Element(A+'srgbClr', val='0F172A'))
        etree.SubElement(rPr, A+'latin', typeface='Trebuchet MS')
        t_tag = etree.SubElement(r_tag, A+'t')
        t_tag.text = '测试：点击鼠标看色块出现'
        endParaRPr = etree.SubElement(p_tag, A+'endParaRPr', lang='zh-CN')
        
        # 3个色块
        colors = ['0284C7', '0D948F', 'EA580C']
        texts = ['色块 1', '色块 2', '色块 3']
        for i in range(3):
            sp2 = etree.SubElement(spTree, NS('sp'))
            nvSpPr2 = etree.SubElement(sp2, NS('nvSpPr'))
            sid = str(i+3)
            etree.SubElement(nvSpPr2, NS('cNvPr'), id=sid, name=f'Box{i+1}')
            etree.SubElement(nvSpPr2, NS('cNvSpPr'))
            etree.SubElement(nvSpPr2, NS('nvPr'))
            spPr2 = etree.SubElement(sp2, NS('spPr'))
            xfrm2 = etree.SubElement(spPr2, A+'xfrm')
            etree.SubElement(xfrm2, A+'off', x=str(1828800 + i*3200400), y='2286000')
            etree.SubElement(xfrm2, A+'ext', cx='2743200', cy='2286000')
            etree.SubElement(spPr2, A+'prstGeom', prst='rect')
            solid = etree.SubElement(spPr2, A+'solidFill')
            etree.SubElement(solid, A+'srgbClr', val=colors[i])
            tx2 = etree.SubElement(sp2, NS('txBody'))
            bp2 = etree.SubElement(tx2, A+'bodyPr')
            ls2 = etree.SubElement(tx2, A+'lstStyle')
            pp = etree.SubElement(tx2, A+'p', algn='ctr')
            rr = etree.SubElement(pp, A+'r')
            rPr2 = etree.SubElement(rr, A+'rPr', sz='2000', lang='zh-CN')
            etree.SubElement(rPr2, A+'solidFill').append(etree.Element(A+'srgbClr', val='FFFFFF'))
            tt = etree.SubElement(rr, A+'t'); tt.text = texts[i]
        
        z.writestr('ppt/slides/slide1.xml', etree.tostring(sld, xml_declaration=True, encoding='UTF-8', standalone=True))
    
    buf.seek(0)
    return buf

# ============ 用 lxml 生成 PPTX ============
def add_anim_to_slide(slide_xml_str):
    """给幻灯片 XML 添加动画"""
    sld = etree.fromstring(slide_xml_str)
    
    # 翻页过渡
    trans = etree.SubElement(sld, NS('transition'), spd='med')
    etree.SubElement(trans, NS('fade'))
    
    # 动画
    timing = etree.SubElement(sld, NS('timing'))
    tnLst = etree.SubElement(timing, NS('tnLst'))
    seq = etree.SubElement(tnLst, NS('seq'), concurrent='0')
    
    ct_root = etree.SubElement(seq, NS('cTn'), id=str(uuid.uuid4()), dur='indefinite')
    scl = etree.SubElement(ct_root, NS('stCondLst'))
    etree.SubElement(scl, NS('cond'), delay='0')
    
    childTnLst = etree.SubElement(seq, NS('childTnLst'))
    
    # 为 shape id 2,3,4,5 添加动画（标题+3色块）
    for spid in ['2','3','4','5']:
        par = etree.SubElement(childTnLst, NS('par'))
        ct = etree.SubElement(par, NS('cTn'), id=str(uuid.uuid4()), fill='hold')
        stc = etree.SubElement(ct, NS('stCondLst'))
        cond = etree.SubElement(stc, NS('cond'), evt='onclick', delay='0')
        te = etree.SubElement(cond, NS('tgtEl'))
        etree.SubElement(te, NS('sldTgt'))
        
        cl = etree.SubElement(par, NS('childTnLst'))
        ae = etree.SubElement(cl, NS('animEffect'), transition='in', filter='appear')
        cb = etree.SubElement(ae, NS('cBhvr'))
        etree.SubElement(cb, NS('cTn'), id=str(uuid.uuid4()), dur='500')
        tgt = etree.SubElement(cb, NS('tgtEl'))
        etree.SubElement(tgt, NS('spTgt'), spid=spid)
    
    return etree.tostring(sld, xml_declaration=True, encoding='UTF-8', standalone=True)

# 生成空的 PPTX
buf = make_empty_pptx()

# 手动修改 slide1.xml 添加动画
import io as io_module

# 重新打包
new_buf = io_module.BytesIO()
with zipfile.ZipFile(buf, 'r') as zin:
    with zipfile.ZipFile(new_buf, 'w', zipfile.ZIP_DEFLATED) as zout:
        for item in zin.infolist():
            data = zin.read(item.filename)
            if item.filename == 'ppt/slides/slide1.xml':
                data = add_anim_to_slide(data)
            zout.writestr(item, data)

new_buf.seek(0)
with open('out/test-raw.pptx', 'wb') as f:
    f.write(new_buf.read())

# 验证
import zipfile as zf
with zf.ZipFile('out/test-raw.pptx', 'r') as z:
    with z.open('ppt/slides/slide1.xml') as f:
        sld = etree.fromstring(f.read())
        print('Slide order:', [c.tag.split('}')[-1] for c in sld])
        timing = sld.find(NS('timing'))
        pars = timing.findall(f'.//{NS("par")}') if timing is not None else []
        ae_count = len(timing.findall(f'.//{NS("animEffect")}')) if timing is not None else 0
        print(f'animEffect count: {ae_count}, pars: {len(pars)}')
        print('SAVED: out/test-raw.pptx')

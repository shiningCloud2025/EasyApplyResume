import zipfile
from lxml import etree

P = 'http://schemas.openxmlformats.org/presentationml/2006/main'

def get_slide_xml(path):
    with zipfile.ZipFile(path, 'r') as z:
        with z.open('ppt/slides/slide1.xml') as f:
            return etree.fromstring(f.read())

# 读取两个文件的 slide1
test = get_slide_xml('out/native-anim.pptx')
v10 = get_slide_xml('out/EasyApplyResume-项目介绍-v10.pptx')

print("=== TEST FILE (PowerPoint generated) ===")
test_timing = test.find(f'{{{P}}}timing')
if test_timing is not None:
    # 打印前 2 个 par
    pars = test_timing.findall(f'.//{{{P}}}par')
    print(f"Total pars: {len(pars)}")
    for i, p in enumerate(pars[:2]):
        print(f"\n--- par {i+1} ---")
        print(etree.tostring(p, pretty_print=True, encoding='unicode')[:800])

print("\n\n=== V10 FILE (our generated) ===")
v10_timing = v10.find(f'{{{P}}}timing')
if v10_timing is not None:
    pars = v10_timing.findall(f'.//{{{P}}}par')
    print(f"Total pars: {len(pars)}")
    for i, p in enumerate(pars[:2]):
        print(f"\n--- par {i+1} ---")
        print(etree.tostring(p, pretty_print=True, encoding='unicode')[:800])

# 关键：检查 transition
print("\n\n=== TRANSITION ===")
test_trans = test.find(f'{{{P}}}transition')
v10_trans = v10.find(f'{{{P}}}transition')
print(f"Test has transition: {test_trans is not None}")
print(f"V10 has transition: {v10_trans is not None}")
if test_trans is not None:
    print(f"Test transition: {etree.tostring(test_trans, encoding='unicode')}")
if v10_trans is not None:
    print(f"V10 transition: {etree.tostring(v10_trans, encoding='unicode')}")

# 检查元素顺序
print("\n\n=== ELEMENT ORDER ===")
print("Test order:", [c.tag.split('}')[-1] for c in test])
print("V10 order:", [c.tag.split('}')[-1] for c in v10])

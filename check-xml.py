import zipfile
from lxml import etree

with zipfile.ZipFile('out/test-anim-v3.pptx', 'r') as z:
    with z.open('ppt/slides/slide1.xml') as f:
        xml = f.read()

root = etree.fromstring(xml)
P = 'http://schemas.openxmlformats.org/presentationml/2006/main'

timing = root.find(f'{{{P}}}timing')
if timing is not None:
    parent = timing.getparent()
    children = list(parent)
    print(f'Timing parent: {parent.tag}')
    print(f'Children: {len(children)}')
    for i, child in enumerate(children):
        print(f'  [{i}] {child.tag}')
    transitions = timing.findall(f'.//{{{P}}}par')
    trans_el = root.find(f'{{{P}}}transition')
    print(f'\nTransition present: {trans_el is not None}')
    print(f'par count in timing: {len(transitions)}')
else:
    print('NO TIMING FOUND!')

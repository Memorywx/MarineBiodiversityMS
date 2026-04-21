USE marine_biodiversity;
SET NAMES utf8mb4;

-- 插入生态系统
INSERT IGNORE INTO ecosystem (id, name, type, description, geo_range, environment_features, create_time, update_time) VALUES
(1, '珊瑚礁', 'coral_reef', '由珊瑚虫骨骼堆积形成的海洋生态系统，生物多样性极高', '热带、亚热带浅海海域，中国主要分布于南海', '水温20-28℃，清澈浅水，高光照', NOW(), NOW()),
(2, '红树林', 'mangrove', '热带、亚热带海岸潮间带特有的木本植物群落', '中国东南沿海、海南岛、雷州半岛', '高盐度、缺氧土壤、潮汐影响', NOW(), NOW()),
(3, '海草床', 'seagrass_bed', '由海草植物构成的浅海底植被生态系统', '温带和热带浅海，中国沿海均有分布', '浅水、泥沙底质、光照充足', NOW(), NOW()),
(4, '深海平原', 'deep_sea', '大陆坡以外的平坦海底区域，水深通常超过2000米', '南海深海区域', '高压、低温、黑暗、食物匮乏', NOW(), NOW());

-- 插入物种（create_by = 2 对应 researcher）
INSERT IGNORE INTO species (id, chinese_name, scientific_name, phylum, class_name, order_name, family, genus, species, morphological_features, living_habits, distribution, distribution_lat, distribution_lng, protection_level, iucn_status, is_public, create_by, create_time, update_time) VALUES
(1, '中华白海豚', 'Sousa chinensis', '脊索动物门', '哺乳纲', '鲸偶蹄目', '海豚科', '白海豚属', '中华白海豚',
 '体呈纺锤形，体色随年龄增长由深灰变浅粉白，吻部短钝，背鳍基部形成驼峰状隆起。成年个体体长2.0-2.8米。',
 '栖息于河口、沿海浅水区，喜欢泥沙底质海域。以鱼类为食，常集群活动。',
 '中国东南沿海、东南亚河口海域，主要分布于珠江口、湛江、厦门附近海域。',
 21.2708, 110.3594, '国家一级', 'VU', 1, 2, NOW(), NOW()),

(2, '绿海龟', 'Chelonia mydas', '脊索动物门', '爬行纲', '龟鳖目', '海龟科', '海龟属', '绿海龟',
 '体型较大，背甲呈心形，表面光滑，颜色为橄榄绿至褐色。成年体重可达100-190公斤。',
 '以海草和藻类为食，长距离洄游于觅食地和产卵沙滩之间。',
 '全球热带、亚热带海域，中国分布于南海诸岛及广东沿海。',
 22.1234, 113.7890, '国家一级', 'EN', 1, 2, NOW(), NOW()),

(3, '眼斑双锯鱼', 'Amphiprion ocellaris', '脊索动物门', '辐鳍鱼纲', '鲈形目', '雀鲷科', '双锯鱼属', '眼斑双锯鱼',
 '体色鲜艳，橙红色身体上有三条白色环带，体长可达11厘米。',
 '与海葵共生，栖息于珊瑚礁浅水区，具有领地性。',
 '印度-太平洋热带海域，中国主要分布于南海珊瑚礁区域。',
 18.2000, 109.5000, '无', 'LC', 1, 2, NOW(), NOW()),

(4, '鲸鲨', 'Rhincodon typus', '脊索动物门', '软骨鱼纲', '须鲨目', '鲸鲨科', '鲸鲨属', '鲸鲨',
 '世界上最大的鱼类，体长可达12-18米，体表有独特的白色斑点和条纹。',
 '滤食性，以浮游生物、小鱼和磷虾为食，常独自活动。',
 '全球热带、温带海域，中国分布于南海和东海。',
 21.5833, 111.9667, '国家二级', 'EN', 1, 2, NOW(), NOW()),

(5, '中国鲎', 'Tachypleus tridentatus', '节肢动物门', '肢口纲', '剑尾目', '鲎科', '东方鲎属', '中国鲎',
 '身体分为头胸部和腹部，头胸部呈马蹄形，尾部有剑状尾节（尾剑）。体长可达60厘米。',
 '栖息于潮间带至浅海泥沙底质海域，以蠕虫、软体动物为食。',
 '中国东南沿海、日本、东南亚海域。',
 21.2708, 110.3594, '国家二级', 'EN', 1, 2, NOW(), NOW()),

(6, '鹦鹉螺', 'Nautilus pompilius', '软体动物门', '头足纲', '鹦鹉螺目', '鹦鹉螺科', '鹦鹉螺属', '鹦鹉螺',
 '具有美丽的螺旋形外壳，外壳内部被分隔成多个气室，体色为白色带红褐色条纹。',
 '夜行性，白天栖息于深海珊瑚礁斜坡，夜间上升到浅水觅食。',
 '印度-太平洋热带海域，中国分布于南海深海区域。',
 18.5000, 112.0000, '国家一级', 'VU', 1, 2, NOW(), NOW()),

(7, '白边真鲨', 'Carcharhinus albimarginatus', '脊索动物门', '软骨鱼纲', '真鲨目', '真鲨科', '真鲨属', '白边真鲨',
 '体型修长，背鳍和尾鳍后缘有明显白色边缘，体长可达3米。',
 '栖息于珊瑚礁外缘和深海陡坡，以鱼类为食。',
 '印度-太平洋热带海域，中国分布于南海珊瑚礁区域。',
 22.5431, 114.4789, '国家二级', 'VU', 1, 2, NOW(), NOW()),

(8, '文昌鱼', 'Branchiostoma belcheri', '脊索动物门', '头索纲', '文昌鱼目', '文昌鱼科', '文昌鱼属', '文昌鱼',
 '体型似小鱼，半透明，无头部分化，体长约5厘米，终生保留脊索。',
 '半底栖生活，潜入浅海沙质海底，只露出头部滤食。',
 '中国沿海浅海沙质海底，以厦门、青岛附近最为著名。',
 24.4798, 118.0894, '无', 'LC', 1, 2, NOW(), NOW());

-- 插入物种图片
INSERT IGNORE INTO species_image (id, species_id, image_url, sort_order, create_time) VALUES
(1, 1, 'https://picsum.photos/seed/dolphin/400/300', 0, NOW()),
(2, 2, 'https://picsum.photos/seed/turtle/400/300', 0, NOW()),
(3, 3, 'https://picsum.photos/seed/clownfish/400/300', 0, NOW()),
(4, 4, 'https://picsum.photos/seed/whaleshark/400/300', 0, NOW()),
(5, 5, 'https://picsum.photos/seed/horseshoecrab/400/300', 0, NOW()),
(6, 6, 'https://picsum.photos/seed/nautilus/400/300', 0, NOW()),
(7, 7, 'https://picsum.photos/seed/shark/400/300', 0, NOW()),
(8, 8, 'https://picsum.photos/seed/lancelet/400/300', 0, NOW());

-- 插入观测记录（create_by = 2）
INSERT IGNORE INTO observation (id, observation_time, location_name, latitude, longitude, ecosystem_id, observer, water_temperature, salinity, ph_value, depth, remarks, create_by, create_time, update_time) VALUES
(1, '2026-03-15 08:30:00', '湛江红树林保护区', 21.2708, 110.3594, 2, '张三、李四', 24.5, 32.1, 8.1, 2.5, '天气晴朗，能见度良好，发现白海豚群体活动', 2, NOW(), NOW()),
(2, '2026-03-22 10:00:00', '珠海万山群岛', 22.1234, 113.7890, 1, '王五', 26.0, 33.5, 8.2, 8.0, '珊瑚礁健康状况良好，鱼类丰富', 2, NOW(), NOW()),
(3, '2026-04-05 14:20:00', '深圳大鹏湾', 22.5431, 114.4789, 1, '赵六、孙七', 25.8, 33.0, 8.15, 12.0, '潜水观测，记录到多种珊瑚礁鱼类', 2, NOW(), NOW()),
(4, '2026-04-12 09:00:00', '阳江闸坡近海', 21.5833, 111.9667, 4, '周八', 23.5, 34.0, 8.05, 45.0, '深海观测站记录，偶遇鲸鲨', 2, NOW(), NOW()),
(5, '2026-02-28 11:30:00', '厦门同安湾', 24.4798, 118.0894, 3, '郑九', 18.5, 30.5, 8.0, 5.0, '低潮时采集文昌鱼样本', 2, NOW(), NOW());

-- 插入观测-物种关联
INSERT IGNORE INTO observation_species (id, observation_id, species_id, estimated_quantity, behavior, remarks, create_time) VALUES
(1, 1, 1, 5, '集群游动', '成年个体，在河口交汇处活动', NOW()),
(2, 1, 5, 12, '觅食', '退潮时在滩涂爬行', NOW()),
(3, 2, 2, 1, '觅食', '在珊瑚礁边缘啃食海草', NOW()),
(4, 2, 3, 8, '守护领地', '与海葵共生，警惕性高', NOW()),
(5, 3, 7, 2, '巡游', '在珊瑚礁外缘巡游', NOW()),
(6, 3, 3, 15, '集群', '大群聚集于珊瑚礁浅水区', NOW()),
(7, 4, 4, 1, '滤食游动', '体长约8米的亚成年个体', NOW()),
(8, 5, 8, 30, '埋栖', '半埋于沙中，只露出口部', NOW());

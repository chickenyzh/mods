# Generate textures for Zombie Evolution mod
Add-Type -AssemblyName System.Drawing

$resDir = "src/main/resources/assets/zombie_evolution"

# ===== 15 Zombie Skin Textures =====
$skinDir = "$resDir/textures/entity/zombie"
New-Item -ItemType Directory -Force -Path $skinDir | Out-Null

$skins = @(
    @{skin="#6B8E5A"; shirt="#4A3520"; pants="#2D2518"; eyes="#800000"},
    @{skin="#8EB87A"; shirt="#5C3A1E"; pants="#3A2A15"; eyes="#600000"},
    @{skin="#5A7A48"; shirt="#8B4513"; pants="#654321"; eyes="#900000"},
    @{skin="#7AA668"; shirt="#4A6B8A"; pants="#2D4055"; eyes="#700000"},
    @{skin="#9CCA88"; shirt="#6B3A4A"; pants="#402030"; eyes="#800000"},
    @{skin="#4A6B3A"; shirt="#3A5A2A"; pants="#2A4020"; eyes="#600000"},
    @{skin="#88B478"; shirt="#555555"; pants="#333333"; eyes="#800000"},
    @{skin="#B8D8A8"; shirt="#8A6B4A"; pants="#5A4A35"; eyes="#700000"},
    @{skin="#6A8E5A"; shirt="#2A4A6A"; pants="#1A2A40"; eyes="#900000"},
    @{skin="#8EB87A"; shirt="#6A2A2A"; pants="#401A1A"; eyes="#800000"},
    @{skin="#A8CC98"; shirt="#4A5A3A"; pants="#2A3520"; eyes="#700000"},
    @{skin="#5A7A48"; shirt="#3A3A3A"; pants="#252525"; eyes="#800000"},
    @{skin="#7AA668"; shirt="#8A6B5A"; pants="#5A4535"; eyes="#600000"},
    @{skin="#9CCA88"; shirt="#5A3A2A"; pants="#3A2518"; eyes="#900000"},
    @{skin="#4A6B3A"; shirt="#6A5A3A"; pants="#4A3A25"; eyes="#800000"}
)

function HexToColor($hex) { return [System.Drawing.Color]::FromArgb(255, [Convert]::ToByte($hex.Substring(1,2),16), [Convert]::ToByte($hex.Substring(3,2),16), [Convert]::ToByte($hex.Substring(5,2),16)) }

$blockSize = 1
for ($i = 0; $i -lt $skins.Length; $i++) {
    $s = $skins[$i]
    $bmp = New-Object System.Drawing.Bitmap 64, 64
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
    $g.Clear([System.Drawing.Color]::Transparent)

    $skinC = New-Object System.Drawing.SolidBrush((HexToColor $s.skin))
    $shirtC = New-Object System.Drawing.SolidBrush((HexToColor $s.shirt))
    $pantsC = New-Object System.Drawing.SolidBrush((HexToColor $s.pants))
    $eyeC = New-Object System.Drawing.SolidBrush((HexToColor $s.eyes))
    $darkC = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(255,0,0,0))

    # Head
    $g.FillRectangle($skinC, 8,0,8,8); $g.FillRectangle($skinC, 24,0,8,8)
    $g.FillRectangle($skinC, 0,8,8,8); $g.FillRectangle($skinC, 16,8,8,8)
    # Eyes
    $g.FillRectangle($eyeC, 10,2,1,1); $g.FillRectangle($eyeC, 13,2,1,1)
    # Mouth
    $g.FillRectangle($darkC, 10,6,4,1)
    # Body
    $g.FillRectangle($shirtC, 20,16,8,12); $g.FillRectangle($shirtC, 32,16,8,12)
    $g.FillRectangle($shirtC, 16,16,4,12); $g.FillRectangle($shirtC, 28,16,4,12)
    # Arms
    $g.FillRectangle($skinC, 40,16,4,12); $g.FillRectangle($skinC, 44,16,4,12)
    $g.FillRectangle($skinC, 32,48,4,12); $g.FillRectangle($skinC, 36,48,4,12)
    # Legs
    $g.FillRectangle($pantsC, 4,16,4,12); $g.FillRectangle($pantsC, 0,16,4,12)
    $g.FillRectangle($pantsC, 12,16,4,12); $g.FillRectangle($pantsC, 8,16,4,12)
    $g.FillRectangle($pantsC, 4,32,4,12); $g.FillRectangle($pantsC, 0,32,4,12)

    $path = "$skinDir/zombie_$i.png"
    $bmp.Save($path, [System.Drawing.Imaging.ImageFormat]::Png)
    $g.Dispose(); $bmp.Dispose()
    Write-Output "Skin: $path"
}

# ===== Item Textures =====
$itemDir = "$resDir/textures/item"
New-Item -ItemType Directory -Force -Path $itemDir | Out-Null

# Helper to create a simple 16x16 item icon
function New-ItemIcon($name, $primaryColor, $secondaryColor, $shape) {
    $bmp = New-Object System.Drawing.Bitmap 16, 16
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
    $g.Clear([System.Drawing.Color]::Transparent)
    $p = New-Object System.Drawing.SolidBrush((HexToColor $primaryColor))
    $s = New-Object System.Drawing.SolidBrush((HexToColor $secondaryColor))

    switch ($shape) {
        "ingot" { $g.FillRectangle($p, 5,2,6,4); $g.FillRectangle($p, 4,6,8,4); $g.FillRectangle($p, 5,10,6,4) }
        "core" { $g.FillEllipse($p, 2,2,12,12); $g.FillEllipse($s, 4,4,8,8); $g.FillEllipse($p, 6,6,4,4) }
        "sword" { $g.FillRectangle($p, 6,0,4,12); $g.FillRectangle($s, 5,12,6,4); $g.FillRectangle($p, 7,0,2,12) }
        "pickaxe" { $g.FillRectangle($p, 5,0,6,4); $g.FillRectangle($s, 6,4,4,6); $g.FillRectangle($p, 5,10,6,6); $g.FillRectangle($s, 7,3,2,8) }
        "axe" { $g.FillRectangle($p, 1,0,14,1); $g.FillRectangle($p, 3,1,10,2); $g.FillRectangle($s, 6,3,4,8); $g.FillRectangle($p, 7,11,2,5) }
        "helmet" { $g.FillRectangle($p, 3,0,10,1); $g.FillRectangle($p, 2,1,12,3); $g.FillRectangle($p, 3,4,10,6); $g.FillRectangle($s, 5,3,6,2) }
        "chestplate" { $g.FillRectangle($p, 3,0,10,14); $g.FillRectangle($s, 5,2,6,10) }
        "leggings" { $g.FillRectangle($p, 3,0,10,3); $g.FillRectangle($p, 2,3,4,10); $g.FillRectangle($p, 10,3,4,10); $g.FillRectangle($s, 3,3,2,8); $g.FillRectangle($s, 11,3,2,8) }
        "boots" { $g.FillRectangle($p, 2,0,4,12); $g.FillRectangle($p, 10,0,4,12); $g.FillRectangle($s, 3,10,2,4); $g.FillRectangle($s, 11,10,2,4); $g.FillRectangle($p, 2,13,12,3) }
        "template" { $g.FillRectangle($s, 3,1,10,14); $g.FillRectangle($p, 5,3,6,4); $g.FillRectangle($p, 5,9,6,4) }
        "egg" { $g.FillEllipse($s, 3,2,10,12); $g.FillEllipse($p, 5,4,6,4); $g.FillEllipse($p, 5,9,6,3) }
        "clock" { $g.FillEllipse($s, 2,2,12,12); $g.FillEllipse($p, 4,4,8,8); $g.FillRectangle([System.Drawing.Brushes]::White, 7,3,2,6); $g.FillRectangle([System.Drawing.Brushes]::White, 7,10,2,2) }
    }

    $path = "$itemDir/$name.png"
    $bmp.Save($path, [System.Drawing.Imaging.ImageFormat]::Png)
    $g.Dispose(); $bmp.Dispose()
    Write-Output "Item: $path"
}

# Item textures (name, primary, secondary, shape)
# Materials
New-ItemIcon "corrupted_core" "#8B0000" "#FF4444" "core"
New-ItemIcon "corrupted_crystal_ingot" "#8B4513" "#CD853F" "ingot"
New-ItemIcon "compound_corrupt_crystal_upgrade_template" "#4A0080" "#8A2BE2" "template"
New-ItemIcon "compound_corrupt_crystal_ingot" "#4A0080" "#9370DB" "ingot"

# Tools - Corrupted
New-ItemIcon "corrupted_crystal_sword" "#8B4513" "#CD853F" "sword"
New-ItemIcon "corrupted_crystal_pickaxe" "#8B4513" "#CD853F" "pickaxe"
New-ItemIcon "corrupted_crystal_axe" "#8B4513" "#CD853F" "axe"

# Armor - Corrupted
New-ItemIcon "corrupted_crystal_helmet" "#8B4513" "#CD853F" "helmet"
New-ItemIcon "corrupted_crystal_chestplate" "#8B4513" "#CD853F" "chestplate"
New-ItemIcon "corrupted_crystal_leggings" "#8B4513" "#CD853F" "leggings"
New-ItemIcon "corrupted_crystal_boots" "#8B4513" "#CD853F" "boots"

# Tools - Compound
New-ItemIcon "compound_corrupt_crystal_sword" "#4A0080" "#9370DB" "sword"
New-ItemIcon "compound_corrupt_crystal_pickaxe" "#4A0080" "#9370DB" "pickaxe"
New-ItemIcon "compound_corrupt_crystal_axe" "#4A0080" "#9370DB" "axe"

# Armor - Compound
New-ItemIcon "compound_corrupt_crystal_helmet" "#4A0080" "#9370DB" "helmet"
New-ItemIcon "compound_corrupt_crystal_chestplate" "#4A0080" "#9370DB" "chestplate"
New-ItemIcon "compound_corrupt_crystal_leggings" "#4A0080" "#9370DB" "leggings"
New-ItemIcon "compound_corrupt_crystal_boots" "#4A0080" "#9370DB" "boots"

# Spawn Eggs
New-ItemIcon "ability_tnt_egg" "#2E8B57" "#8B0000" "egg"
New-ItemIcon "ability_breaker_egg" "#B8860B" "#FF4500" "egg"
New-ItemIcon "ability_builder_egg" "#4682B4" "#87CEEB" "egg"
New-ItemIcon "ability_climber_egg" "#556B2F" "#8FBC8F" "egg"
New-ItemIcon "ability_splitter_egg" "#8B008B" "#FF69B4" "egg"

# Day Setter
New-ItemIcon "day_setter" "#FFC832" "#C8961E" "clock"

# ===== Block Textures =====
$blockDir = "$resDir/textures/block"
New-Item -ItemType Directory -Force -Path $blockDir | Out-Null

$bmp = New-Object System.Drawing.Bitmap 16, 16
$g = [System.Drawing.Graphics]::FromImage($bmp)
$g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
$dark = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(255, 60, 60, 70))
$light = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(255, 90, 90, 100))
$g.Clear([System.Drawing.Color]::FromArgb(255, 50, 50, 60))
for ($x = 0; $x -lt 16; $x+=2) { for ($y = 0; $y -lt 16; $y+=2) {
    if (($x+$y) % 4 -eq 0) { $g.FillRectangle($dark, $x,$y,2,2) } else { $g.FillRectangle($light, $x,$y,2,2) }
}}
$bmp.Save("$blockDir/structure_foundation.png", [System.Drawing.Imaging.ImageFormat]::Png)
$g.Dispose(); $bmp.Dispose()
Write-Output "Block: $blockDir/structure_foundation.png"

# ===== Entity Textures for Ability Zombies =====
$entityDir = "$resDir/textures/entity"
New-Item -ItemType Directory -Force -Path $entityDir | Out-Null

$abilitySkins = @(
    @{name="ability_tnt_zombie"; body="#2E8B57"; shirt="#8B0000"; detail="#FF0000"},
    @{name="ability_breaker_zombie"; body="#B8860B"; shirt="#FF4500"; detail="#8B0000"},
    @{name="ability_builder_zombie"; body="#4682B4"; shirt="#87CEEB"; detail="#00008B"},
    @{name="ability_climber_zombie"; body="#556B2F"; shirt="#8FBC8F"; detail="#2F4F2F"},
    @{name="ability_splitter_zombie"; body="#8B008B"; shirt="#FF69B4"; detail="#4B0082"}
)

foreach ($as in $abilitySkins) {
    $bmp = New-Object System.Drawing.Bitmap 64, 64
    $g = [System.Drawing.Graphics]::FromImage($bmp)
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
    $g.Clear([System.Drawing.Color]::Transparent)

    $bodyC = New-Object System.Drawing.SolidBrush((HexToColor $as.body))
    $shirtC = New-Object System.Drawing.SolidBrush((HexToColor $as.shirt))
    $detailC = New-Object System.Drawing.SolidBrush((HexToColor $as.detail))
    $eyeC = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(255, 255, 0, 0))

    # Head
    $g.FillRectangle($bodyC, 8,0,8,8); $g.FillRectangle($bodyC, 24,0,8,8)
    $g.FillRectangle($bodyC, 0,8,8,8); $g.FillRectangle($bodyC, 16,8,8,8)
    # Eyes
    $g.FillRectangle($eyeC, 10,2,1,1); $g.FillRectangle($eyeC, 13,2,1,1)
    # Body
    $g.FillRectangle($shirtC, 20,16,8,12); $g.FillRectangle($shirtC, 32,16,8,12)
    $g.FillRectangle($shirtC, 16,16,4,12); $g.FillRectangle($shirtC, 28,16,4,12)
    # Arms
    $g.FillRectangle($bodyC, 40,16,4,12); $g.FillRectangle($bodyC, 44,16,4,12)
    $g.FillRectangle($bodyC, 32,48,4,12); $g.FillRectangle($bodyC, 36,48,4,12)
    # Legs
    $g.FillRectangle($detailC, 4,16,4,12); $g.FillRectangle($detailC, 0,16,4,12)
    $g.FillRectangle($detailC, 12,16,4,12); $g.FillRectangle($detailC, 8,16,4,12)
    $g.FillRectangle($detailC, 4,32,4,12); $g.FillRectangle($detailC, 0,32,4,12)

    $path = "$entityDir/$($as.name).png"
    $bmp.Save($path, [System.Drawing.Imaging.ImageFormat]::Png)
    $g.Dispose(); $bmp.Dispose()
    Write-Output "Ability Skin: $path"
}

Write-Output "`n=== All textures generated! ==="

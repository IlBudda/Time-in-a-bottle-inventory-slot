ISTRUZIONI PER LA TEXTURE DELLO SLOT
====================================

Metti la tua immagine PNG personalizzata in questa cartella con il nome:
slot_background.png

Specifiche della texture:
- Dimensioni: 16x16 pixel (dimensione standard del contenuto di uno slot Minecraft)
- Formato: PNG con supporto per trasparenza
- La texture verrà renderizzata CENTRATA nello slot (con bordino standard di Minecraft)

Esempio di posizionamento:
src/main/resources/assets/timeinabottleinventoryslot/textures/gui/slot_background.png

Una volta aggiunta la texture, ricompila la mod con:
./gradlew build

NOTA: Lo slot avrà automaticamente il bordino "scavato" standard di Minecraft,
e la tua texture 16x16 verrà centrata all'interno (come gli altri slot).

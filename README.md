# CHNoteBlock
NoteBlockAPI wrapper for CommandHelper

## Functions

| Function Name     | Returns | Arguments                                 | Description                                     |
| ----------------- | ------- | ----------------------------------------- | ----------------------------------------------- |
| all_nbsongs       | array   |                                           | Returns all of loaded noteblock songs           |
| load_nbsong_async | void    | id, path[, successCallback, failCallback] | Load the song                                   |
| play_nbsong       | void    | player, playData                          | Play the song                                   |
| unload_nbsong     | bool    | id                                        | Unload the song of the given id.                |

## Examples

```javascript
load_nbsong_async('song-id', 'path', closure() {
  play_nbsong(player(), array(
      id: 'song-id',
      type: position,
      location: ploc(player()),
      players: all_players()
  ))
})
```

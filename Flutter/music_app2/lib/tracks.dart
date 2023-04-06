import 'dart:io';
import 'package:hexcolor/hexcolor.dart';
import 'package:flutter/material.dart';
import 'package:flutter_audio_query/flutter_audio_query.dart';
import 'package:just_audio/just_audio.dart';
import 'package:music_app2/screens/music_player.dart';


class Tracks extends StatefulWidget {
  const Tracks({Key? key}) : super(key: key);

  @override
  _TracksState createState() => _TracksState();
}

class _TracksState extends State<Tracks> {

  final FlutterAudioQuery audioQuery = FlutterAudioQuery();
  final _audioPlayer = AudioPlayer();
  List<SongInfo> songs = [];
  int currentIndex = 0;

  final GlobalKey<MusicPlayerState> key = GlobalKey<MusicPlayerState>();

  void getSongs() async {
    songs = await audioQuery.getSongs();
    setState(() {
      songs = songs;
    });
  }

  @override
  void initState() {
    super.initState();
    getSongs();
  }

  void changeTrack(bool isNext){
    if(isNext){
      if(currentIndex != songs.length - 1){
        currentIndex++;
      } else {
        currentIndex = 0;
      }
    }else{
      if(currentIndex != 0){
        currentIndex--;
      }else {
        currentIndex = songs.length - 1;
      }
    }
    key.currentState!.setSong(songs[currentIndex]);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
          gradient: LinearGradient(
              begin: Alignment.topRight,
              end: Alignment.bottomLeft,
              colors: [
                HexColor("#0F2027"),
                HexColor("#203A43"),
                HexColor("#2C5364"),
              ]
          )
      ),
      child: Scaffold(
        backgroundColor: Colors.transparent,
        appBar: AppBar(
          title: Text('Music Player', style: TextStyle(fontFamily: 'Nunito', fontWeight: FontWeight.bold),),
          centerTitle: true,
          backgroundColor: Colors.transparent,
          elevation: 0.0,
        ),

        body: ListView.separated(
          separatorBuilder: (context, index) => const Divider(),
          itemCount: songs.length,
          itemBuilder: (context, index) => ListTile(
            leading: CircleAvatar(
              backgroundImage: songs[index].albumArtwork == null ? AssetImage('assets/images/gradient.jpg') : Image.file(File(songs[index].albumArtwork)) as ImageProvider,
            ),
            title: Text(songs[index].title, style: const TextStyle(fontFamily: 'Nunito', fontWeight: FontWeight.bold),),
            subtitle: Text(songs[index].artist  == '<unknown>' ? 'Unknown Artist' : songs[index].artist, style: const TextStyle(fontFamily: 'Nunito',),),
            onTap: (){
              currentIndex = index;
              Navigator.of(context).push(MaterialPageRoute(builder: (context) => MusicPlayer(songInfo: songs[currentIndex], changeTrack: changeTrack, key: key, audioPlayer: _audioPlayer,)));
            },
          ),

        ),
      ),
    );
  }
}



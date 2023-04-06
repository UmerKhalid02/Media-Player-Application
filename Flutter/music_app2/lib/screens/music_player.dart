import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter_audio_query/flutter_audio_query.dart';
import 'package:just_audio/just_audio.dart';
import 'package:hexcolor/hexcolor.dart';

class MusicPlayer extends StatefulWidget {
  MusicPlayer({required this.songInfo, required this.changeTrack, required this.key, required this.audioPlayer}) : super(key: key);

  final AudioPlayer audioPlayer;
  Function changeTrack;
  SongInfo songInfo;
  final GlobalKey<MusicPlayerState> key;

  @override
  MusicPlayerState createState() => MusicPlayerState();
}

class MusicPlayerState extends State<MusicPlayer> {

  bool isPlaying = false;

  double minValue = 0;
  double maxValue = 0;
  double currentValue = 0;

  String currentTime = '', endTime ='';

  @override
  void initState() {
    super.initState();
    setSong(widget.songInfo);
  }

  void changeStatus(){
    setState(() {
      isPlaying = !isPlaying;
      if(isPlaying){
        widget.audioPlayer.play();
      }else{
        widget.audioPlayer.pause();
      }
    });
  }

  void setSong(SongInfo songInfo) async {
    widget.songInfo = songInfo;
    await widget.audioPlayer.setUrl(widget.songInfo.uri);
    currentValue = minValue;
    maxValue = widget.audioPlayer.duration!.inMilliseconds.toDouble();

    setState(() {
      currentTime = getDuration(currentValue);
      endTime = getDuration(maxValue);
    });
    changeStatus();
    widget.audioPlayer.positionStream.listen((duration) {
      currentValue = duration.inMilliseconds.toDouble();
      setState(() {
        currentTime = getDuration(currentValue);
        if(currentTime == endTime){
          widget.changeTrack(true);
          isPlaying = false;
        }
      });
    });

  }

  String getDuration(double value){
    Duration duration = Duration(milliseconds: value.round());
    
    return [duration.inMinutes, duration.inSeconds].map((element) =>
        element.remainder(60).toString().padLeft(2,'0')).join(':');
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
          title: const Text(
            'Now Playing',
            style: TextStyle(
                fontFamily: 'Nunito',
                fontWeight: FontWeight.bold,
                fontSize: 14
            ),
          ),
          centerTitle: true,
          backgroundColor: Colors.transparent,
          elevation: 0.0,
          leading: IconButton(
            icon: Icon(Icons.arrow_back_ios),
            onPressed: (){
              Navigator.of(context).pop();
            },
          ),
        ),

        body: Container(
          margin: const EdgeInsets.fromLTRB(10, 40, 10, 0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              CircleAvatar(
                backgroundImage: widget.songInfo.albumArtwork == null ? const AssetImage('assets/images/gradient.jpg'):FileImage(File(widget.songInfo.albumArtwork)) as ImageProvider,
                radius: 150,
              ),

              Column(
                children: [
                  Container(
                    margin: const EdgeInsets.fromLTRB(0, 40, 0, 7),
                    child: Text(
                      widget.songInfo.title,
                      style: const TextStyle(
                        fontFamily: 'Nunito',
                        fontWeight: FontWeight.w600,
                        fontSize: 16,
                      ),
                    ),
                  ),

                  Container(
                    margin: const EdgeInsets.fromLTRB(0, 0, 0, 0),
                    child: Text(
                      widget.songInfo.artist == '<unknown>' ? 'Unknown Artist' : widget.songInfo.artist,
                      style: const TextStyle(
                        fontFamily: 'Nunito',
                        fontWeight: FontWeight.w200,
                        fontSize: 14,
                      ),
                    ),
                  ),
                ],
              ),

              Container(
                transform: Matrix4.translationValues(0, -5, 0),
                // margin: const EdgeInsets.fromLTRB(5, 0, 5, 0),
                child: Row(
                  children: [
                    Text(
                      currentTime,
                      style: const TextStyle(
                        fontFamily: 'Nunito',
                        fontWeight: FontWeight.w200,
                        fontSize: 14,
                      ),
                    ),
                    Expanded(
                      child: Slider(
                        activeColor: Colors.white,
                        inactiveColor: Colors.grey[700],
                        min: minValue,
                        max: maxValue,
                        value: currentValue,
                        onChanged: (value){
                          currentValue = value;
                          widget.audioPlayer.seek(Duration(milliseconds: currentValue.round()));
                        },
                      ),
                    ),
                    Text(
                      endTime,
                      style: const TextStyle(
                        fontFamily: 'Nunito',
                        fontWeight: FontWeight.w200,
                        fontSize: 14,
                      ),
                    ),
                  ],
                ),
              ),

              Container(
                // margin: const EdgeInsets.fromLTRB(0, 0, 0, 0),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    GestureDetector(
                      child: const Icon(Icons.skip_previous_rounded, size: 55,),
                      behavior: HitTestBehavior.translucent,
                      onTap: (){
                        isPlaying = !isPlaying;
                        widget.changeTrack(false);
                      },
                    ),
                    GestureDetector(
                      child: Icon(isPlaying?Icons.pause_circle_filled_rounded : Icons.play_circle_fill_rounded, size: 75,),
                      behavior: HitTestBehavior.translucent,
                      onTap: (){
                        changeStatus();
                      },
                    ),
                    GestureDetector(
                      child: const Icon(Icons.skip_next_rounded, size: 55,),
                      behavior: HitTestBehavior.translucent,
                      onTap: (){
                        isPlaying = !isPlaying;
                        widget.changeTrack(true);
                      },
                    ),
                  ],
                ),
              )
            ],
          ),
        ),

      ),
    );
  }
}

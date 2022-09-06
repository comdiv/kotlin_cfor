package main

import "fmt"

func main() {
	data := []byte{
		0x8E, // goto cell 13
		0x13,
		'w',
		'o',
		'r',
		0x22,
		'h',
		'a',
		'h',
		'a',
		0x12,
		'l',
		'd',
		0xFF, //exit
		0x16,
		'h',
		'e',
		'l',
		'l',
		'o',
		' ',
		0x81,
	}
	result := make([]byte, 0)
	for i := 0; i < len(data); i++ {
		code := data[i]
		cmd, arg := code>>4, code&0x0F
		if cmd == 0xF {
			break
		}
		if cmd == 0x08 {
			i = int(arg) - 1 //will be i++
			continue
		}
		i += int(cmd * arg)
		if cmd != 1 { // считываем только байты
			continue
		}
		result = append(result, data[i-int(arg)+1:i+1]...)
	}
	resultString := string(result)
	if resultString != "hello world" {
		panic(fmt.Errorf(" `%s` != `hello world` !", resultString))
	}
	fmt.Println(resultString)
}

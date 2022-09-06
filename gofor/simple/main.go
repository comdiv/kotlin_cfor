package main

import "fmt"

func main() {
	data := []byte{
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
	}
	result := make([]byte, 0)
	for i := 0; i < len(data); i++ {
		code := data[i]
		size, count := code>>4, code&0x0F
		i += int(size * count)
		if size != 1 { // считываем только байты
			continue
		}
		result = append(result, data[i-int(count)+1:i+1]...)
	}
	resultString := string(result)
	if resultString != "world" {
		panic(fmt.Errorf(" `%s` != `world` !", resultString))
	}
	fmt.Println(resultString)
}

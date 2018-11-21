import React from 'react'
import { configure, shallow } from 'enzyme'
import toJson from 'enzyme-to-json'
import Adapter from 'enzyme-adapter-react-16'
import { TouchableOpacity } from 'react-native'

import MainMenuScreen from '../../screens/MainMenuScreen'

configure({ adapter: new Adapter() })

describe('MainMenuScreen snapshot', () => {
  it('renders MainMenuScreen correctly', () => {
    const wrapper = shallow(<MainMenuScreen
      navigation={{ navigate: jest.fn() }} />)
    expect(toJson(wrapper)).toMatchSnapshot()
    wrapper.find(TouchableOpacity).at(0).props().onPress()
    wrapper.find(TouchableOpacity).at(1).props().onPress()
    wrapper.unmount()
  })
})

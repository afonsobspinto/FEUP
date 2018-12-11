import React from 'react'
import { StyleSheet } from 'react-native'
import { Container, Header, Content, Footer, FooterTab, Button, Icon, Title, Body, Text } from 'native-base'
import { RoutinesScreen } from './RoutinesScreen'
import RewardsScreen from './RewardsScreen'
import SettingsScreen from './SettingsScreen'
import ActionButton from 'react-native-action-button'
import PropTypes from 'prop-types'

export default class ParentMainMenuScreen extends React.Component {
  constructor (props) {
    super(props)

    this.state = {
      selectedTab: 'activity',
      title: 'Atividade'
    }
  }

  renderSelectedTab = () => {
    switch (this.state.selectedTab) {
      case 'activity':
        return (<Content />)
      case 'routines':
        return (<RoutinesScreen navigation={this.props.navigation} />)
      case 'rewards':
        return (<RewardsScreen />)
      case 'settings':
        return (<SettingsScreen />)
    }
  }

  getCorrespondingActionButton = () => {
    if (this.state.selectedTab === 'activity') {
      return (
        <ActionButton style={styles.actionButton} buttonColor='rgba(231,76,60,1)'>
          <ActionButton.Item buttonColor='#9b59b6' title='Criar atividade' onPress={() => this.props.navigation.navigate('ActivityFormScreen')}>
            <Icon name='md-list-box' style={styles.actionButtonIcon} />
          </ActionButton.Item>
          <ActionButton.Item buttonColor='#1abc9c' title='Criar rotina' onPress={() => this.props.navigation.navigate('RoutineFormScreen')}>
            <Icon name='md-calendar' style={styles.actionButtonIcon} />
          </ActionButton.Item>
        </ActionButton>
      )
    } else if (this.state.selectedTab === 'rewards') {
      return (
        <ActionButton style={styles.actionButton} buttonColor='rgba(231,76,60,1)'>
          <ActionButton.Item buttonColor='#9b59b6' title='Criar prémio' onPress={() => this.props.navigation.navigate('ActivityFormScreen')}>
            <Icon name='md-trophy' style={styles.actionButtonIcon} />
          </ActionButton.Item>
        </ActionButton>
      )
    }
  }

  render () {
    return (
      <Container>
        <Header>
          <Body>
            <Title>{this.state.title}</Title>
          </Body>
        </Header>
        {this.renderSelectedTab()}
        <Footer>
          <FooterTab>
            <Button active={this.state.selectedTab === 'activity'}
              onPress={() => this.setState({ selectedTab: 'activity', title: 'Atividade' })} >
              <Icon name='md-filing' />
              <Text>Atividades</Text>
            </Button>
            <Button active={this.state.selectedTab === 'routines'}
              onPress={() => this.setState({ selectedTab: 'routines', title: 'Gerir Rotinas' })} >
              <Icon name='md-apps' />
              <Text>Rotinas</Text>
            </Button>
            <Button active={this.state.selectedTab === 'rewards'}
              onPress={() => this.setState({ selectedTab: 'rewards', title: 'Recompensas' })} >
              <Icon name='md-trophy' />
              <Text>Prémios</Text>
            </Button>
            <Button active={this.state.selectedTab === 'settings'}
              onPress={() => this.setState({ selectedTab: 'settings', title: 'Definições' })}>
              <Icon name='md-settings' />
              <Text>Definições</Text>
            </Button>
          </FooterTab>
        </Footer>
        {this.getCorrespondingActionButton()}
      </Container>
    )
  }
}

ParentMainMenuScreen.propTypes = {
  navigation: PropTypes.object.isRequired
}

const styles = StyleSheet.create({
  actionButtonIcon: {
    fontSize: 20,
    height: 22,
    color: 'white'
  },
  actionButton: {
    marginBottom: 55
  }
})
